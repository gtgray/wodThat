package tk.atna.wodthat.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import tk.atna.wodthat.R
import tk.atna.wodthat.databinding.FragmentExerciseDetailsBinding
import tk.atna.wodthat.internal.*
import tk.atna.wodthat.model.ExerciseDetailed
import tk.atna.wodthat.stuff.Log
import tk.atna.wodthat.stuff.discoverVideoId

class ExerciseDetailsFragment() : BottomNavFragment() {

    private val LAYOUT = R.layout.fragment_exercise_details

    private lateinit var binding: FragmentExerciseDetailsBinding

    private val viewModel : ExercisesViewModel by viewModels({ requireParentFragment() })

    private lateinit var exerciseCode : String
    private lateinit var exerciseName : String
    private var exerciseDetailed : ExerciseDetailed? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        //
        pullData()
    }

    override fun onProcessArguments(data: Bundle) {
        val args = ExerciseDetailsFragmentArgs.fromBundle(data)
        when {
            // navigate back
            (args.exerciseCode == null || args.exerciseName == null) -> onUpPressed()
            // general case
            else -> {
                exerciseCode = args.exerciseCode
                exerciseName = args.exerciseName
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false)
        binding.inProgress = true
        lifecycle.addObserver(binding.vgVideo)
        //
        populateViews()
        return binding.root
    }

    override fun showProgressInternal() {
        binding.inProgress = true
    }

    override fun hideProgressInternal() {
        binding.inProgress = false
    }

    private fun populateViews() {
        binding.tvToolbarTitle.text = exerciseName
        binding.vgToolbar.setNavigationOnClickListener {
            onUpPressed()
        }
        //
        exerciseDetailed?.let { exercise ->
            with(binding) {
                tvModality.text = exercise.modality

                vgChipGroup.addIconedChips(R.drawable.svg_level, exercise.level.trim())
                vgChipGroup.addIconedChips(R.drawable.svg_movement, exercise.classExercise.trim())
                vgChipGroup.addIconedChips(R.drawable.svg_equipment, exercise.equipment.trim())

                // add teaser if present
                if (vgTeaserGroup.visible(exercise.teaser.trim().isNotEmpty()))
                    tvTeaser.htmlText = exercise.teaser

//                vgVideo.player = initializePlayer(exercise.videoLink) // exoplayer init
                val videoId = discoverVideoId(exercise.videoLink)
                if (vgVideoGroup.visible(videoId != null))
                    vgVideo.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(videoId!!, 0f)
                        }
                    })

                // add desc if present
                if (vgDescGroup.visible(exercise.description.trim().isNotEmpty()))
                    tvDesc.htmlText = exercise.description
            }
        }
    }

//    private fun initializePlayer(videoLink: String?): ExoPlayer? {
//        return videoLink
//            .takeIf { link -> !link.isNullOrBlank() }
//            ?.let {
//                val player = SimpleExoPlayer.Builder(requireContext()).build()
//                val uri = Uri.parse(videoLink)
//                val dataSourceFactory = DefaultDataSourceFactory(requireContext(), "exoplayer_android")
//                val mediaSource = DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
//                //
//                player.playWhenReady = true
//                player.prepare(mediaSource, false, false)
//                //
//                return player
//            }
//    }

    private fun pullData() {
        viewModel.getExerciseDetails(exerciseCode).observe(this, Observer { resource ->

            Log.w("----------------- DATA ${resource.resultCode}")

            when (resource.resultCode) {
                ResultCode.IN_PROGRESS -> showProgress()
                ResultCode.SUCCESS -> {
                    hideProgress()
                    exerciseDetailed = resource.data
                    populateViews()
                }
                else -> {
                    hideProgress()
                    binding.error = true
                }
            }
        })
    }

}