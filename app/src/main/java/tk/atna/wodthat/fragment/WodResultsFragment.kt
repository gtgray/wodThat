package tk.atna.wodthat.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import tk.atna.wodthat.R
import tk.atna.wodthat.adapter.WodResultsAdapter
import tk.atna.wodthat.adapter.WodsAdapter
import tk.atna.wodthat.databinding.FragmentWodDetailsBinding
import tk.atna.wodthat.databinding.FragmentWodResultsBinding
import tk.atna.wodthat.internal.*
import tk.atna.wodthat.model.*
import tk.atna.wodthat.stuff.*

class WodResultsFragment() : PrimaryBottomSheetFragment() {

    private val LAYOUT = R.layout.fragment_wod_results

    private lateinit var binding: FragmentWodResultsBinding

    private val viewModel : WodsViewModel by viewModels({ requireActivity() })

    private lateinit var wodCode : String
//    private lateinit var wodName : String

    private lateinit var adapter: WodResultsAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        //
        initAdapter()
        pullData()
    }

//    override fun onProcessArguments(data: Bundle) {
//        val args = WodResultsFragmentArgs.fromBundle(data)
//        when {
//            // navigate back
//            (args.wodCode == null/* || args.wodName == null*/) -> dismiss()
//            // general case
//            else -> {
//                wodCode = args.wodCode
////                wodName = args.wodName
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false)
//        binding.inProgress = true
        //
        val behavior = (dialog as BottomSheetDialog).behavior
        behavior.peekHeight = dpToPixels(requireContext(), 56)
        behavior.skipCollapsed = false
        //
        populateViews()
        return binding.root
    }

//    override fun showProgressInternal() {
//        binding.inProgress = true
//    }

//    override fun hideProgressInternal() {
//        binding.inProgress = false
//    }

    private fun populateViews() {
        binding.vgToolbar.title = wodCode
//        binding.vgToolbar.setNavigationOnClickListener {
//            onUpPressed()
//        }
        //
        with(binding) {
            rvResults.setHasFixedSize(true)
            rvResults.adapter = adapter
            (rvResults.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

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
        viewModel.getWodResults(wodCode).observe(this, Observer { results ->

            Log.w("----------------- DATA ${results?.size}")

            when {
                results.isNotEmpty() -> adapter.setData(results)
                else -> binding.error = true
            }
        })
    }

}