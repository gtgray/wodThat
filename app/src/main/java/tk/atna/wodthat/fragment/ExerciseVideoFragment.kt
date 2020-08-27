package tk.atna.wodthat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import tk.atna.wodthat.R
import tk.atna.wodthat.databinding.FragmentExerciseVideoBinding

class ExerciseVideoFragment() : PrimaryBottomSheetFragment() {

    private val LAYOUT = R.layout.fragment_exercise_video

    private lateinit var binding: FragmentExerciseVideoBinding

    private lateinit var videoId : String


    override fun onProcessArguments(data: Bundle) {
        val args = ExerciseVideoFragmentArgs.fromBundle(data)
        when {
            // dismiss
            (args.videoId == null) -> dismiss()
            // general case
            else -> {
                videoId = args.videoId
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false)
        binding.clickHandler = this
        lifecycle.addObserver(binding.vgVideo)
        //
//        val behavior = (dialog as BottomSheetDialog).behavior
//        behavior.skipCollapsed = true
        //
        populateViews()
        return binding.root
    }

    private fun populateViews() {
        with(binding) {
            vgVideo.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        }
    }

    fun onClose() {
        dismiss()
    }

}