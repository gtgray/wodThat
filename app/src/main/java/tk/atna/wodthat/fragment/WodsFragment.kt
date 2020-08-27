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
import com.google.android.material.snackbar.Snackbar
import tk.atna.wodthat.R
import tk.atna.wodthat.adapter.WodsAdapter
import tk.atna.wodthat.databinding.FragmentWodsBinding
import tk.atna.wodthat.internal.FirstLastItemDecoration
import tk.atna.wodthat.internal.ResultCode
import tk.atna.wodthat.internal.WodsViewModel
import tk.atna.wodthat.internal.setNonScrollableContentCallback
import tk.atna.wodthat.model.Wod
import tk.atna.wodthat.stuff.Log
import tk.atna.wodthat.stuff.snackbar

class WodsFragment() : BottomNavFragment() {

    private val LAYOUT = R.layout.fragment_wods

    private lateinit var binding: FragmentWodsBinding

    private val viewModel: WodsViewModel by viewModels({ requireActivity() })

    private lateinit var adapter: WodsAdapter

    companion object {
        private const val DEFAULT_TOP_PADDING_DP = 8
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //
        initAdapter()
        pullData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false)
        //
        populateViews()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //
        binding.rvWods.adapter = null
    }

    override fun showProgressInternal() {
        binding.inProgress = true
    }

    override fun hideProgressInternal() {
        binding.inProgress = false
    }

    private fun populateViews() {
        //
        // todo: init toolbar here
        //
        with(binding) {
            rvWods.setHasFixedSize(true)
            rvWods.adapter = adapter
            (rvWods.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            rvWods.addItemDecoration(
                FirstLastItemDecoration.create(requireContext(), DEFAULT_TOP_PADDING_DP)
            )
            // prevents toolbar scrolling for non-scrollable content (less than screen height)
            rvWods.setNonScrollableContentCallback()
        }

    }

    private fun initAdapter() {
        adapter = WodsAdapter(requireContext(), object : WodsAdapter.ItemActionListener {
            override fun onItemClick(item: Wod) {
                getNavController()?.navigate(
                    WodsFragmentDirections.actionWodsToDetails(item.code, item.name)
                )
            }

            override fun onSaveClick(item: Wod) {
                snackbar(view, "Log in to add wod '${item.name}' to saved", Snackbar.LENGTH_LONG)
            }
        })
    }

    private fun pullData() {
        viewModel.getWods().observe(this, Observer { resource ->

            Log.w("----------------- DATA ${resource.resultCode}")

            when (resource.resultCode) {
                ResultCode.IN_PROGRESS -> showProgress()
                ResultCode.SUCCESS -> {
                    hideProgress()
                    adapter.setData(resource.data!!/*.take(4)*/)

//                    Log.w("----------------- SET ONE")
//
//                    Handler().postDelayed({
//                        adapter.setData(resource.data!!)
//
//                        Log.w("----------------- SET TWO")
//
//                        Handler().postDelayed({
//                            adapter.setData(resource.data!!.take(3))
//
//                            Log.w("----------------- SET THREE")
//
//                        }, 5_000)
//
//                    }, 5_000)


                }
                else -> {
                    hideProgress()
                    binding.error = true
                }
            }
        })
    }

}