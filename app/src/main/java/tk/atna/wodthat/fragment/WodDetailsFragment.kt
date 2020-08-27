package tk.atna.wodthat.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import tk.atna.wodthat.R
import tk.atna.wodthat.adapter.WodResultsAdapter
import tk.atna.wodthat.databinding.FragmentWodDetailsBinding
import tk.atna.wodthat.internal.*
import tk.atna.wodthat.model.*
import tk.atna.wodthat.stuff.*

class WodDetailsFragment() : BottomNavFragment() {

    private val LAYOUT = R.layout.fragment_wod_details

    private lateinit var binding: FragmentWodDetailsBinding
    private var bottomSheetBehavior: BottomSheetLayoutBehavior? = null

    private val viewModel : WodsViewModel by viewModels({ requireActivity() })

    private lateinit var wodCode : String
    private lateinit var wodName : String
    private var wodDetailed : WodDetailed? = null

    private lateinit var adapter: WodResultsAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        //
        pullData()
    }

    override fun onProcessArguments(data: Bundle) {
        val args = WodDetailsFragmentArgs.fromBundle(data)
        when {
            // navigate back
            (args.wodCode == null || args.wodName == null) -> onUpPressed()
            // general case
            else -> {
                wodCode = args.wodCode
                wodName = args.wodName
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

    fun onCloseClick() {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun populateViews() {
        binding.tvToolbarTitle.text = wodName
        binding.vgToolbar.setNavigationOnClickListener {
            onUpPressed()
        }
        //
        wodDetailed?.let { wod ->
            with(binding) {

                if (wod.author.avatarLink != null)
                    placeImage(ivAuthor, wod.author.avatarLink)
                // add author name
                tvAuthor.text = wod.author.fullName

                // add rating if there
                if (tvRating.visible(wod.summaryRating > 0))
                    tvRating.text = String.format("%s/10", wod.summaryRating)

                tvModality.text = wod.modality
                tvHeadline.text = formatHeadline(wod.headline)
                // clear exercises list
                vgWodItems.removeAllViews()
                // add general repetitions scheme if there
                wod.generalScheme?.let {
                    addTextLine(vgWodItems, wod.generalScheme, R.dimen.text_16)
                }
                // add exercises themself
                addWodExercises(vgWodItems, wod.exercises, R.dimen.text_16)

                // show avg duration if there
                if (vgDurationGroup.visible(wod.duration > 0))
                    tvDuration.text = buildText(
                        " ",
                        "Avg duration:",
                        addColoredSpan(
                            TimestampUtils.secsToDuration(wod.duration),
                            getColor(context, R.color.clr_black_85)
                        )
                    )

                // add desc if present
                val desc = discoverHtmlWodDesc(wod).trim()
                if (vgDescGroup.visible(desc.isNotEmpty()))
                    tvDesc.htmlText = desc

                // add tags as chips
                vgChipGroup.addChips(wod.tags.trim())

                // add video if there
                val videoId = discoverVideoId(wod.videoLink)
                if (vgVideoGroup.visible(videoId != null))
                    vgVideo.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(videoId!!, 0f)
                        }
                    })

                // show results count if there
                if (vgResults.visible(wod.resultsCount > 0)) {
                    bottomSheetBehavior = BottomSheetLayoutBehavior.from(vgResults)
                    bottomSheetBehavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                                rvResults.scrollToPosition(0)
                                rvResults.performAccessibilityAction(
                                    AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD,
                                    null
                                )
                            }
                            //
                            btnClose.visible(newState == BottomSheetBehavior.STATE_EXPANDED)
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {}

                    })
                    //
                    tvResults.text = String.format("%d results", wod.resultsCount)
                    //
                    initAdapter()
                    adapter.setData(wod.results)
                    //
                    rvResults.setHasFixedSize(true)
                    rvResults.adapter = adapter
                    (rvResults.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
                }

            }
        }
    }

    private fun initAdapter() {
        adapter = WodResultsAdapter(requireContext(), object : WodResultsAdapter.ItemActionListener {
            override fun onItemClick(item: Wod) {
                // todo: if there
            }
        })
    }

    private fun pullData() {
        viewModel.getWodDetails(wodCode).observe(this, Observer { resource ->

            Log.w("----------------- DATA ${resource.resultCode}")

            when (resource.resultCode) {
                ResultCode.IN_PROGRESS -> showProgress()
                ResultCode.SUCCESS -> {
                    hideProgress()
                    wodDetailed = resource.data
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