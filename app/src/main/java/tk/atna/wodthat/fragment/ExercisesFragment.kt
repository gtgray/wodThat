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
import tk.atna.wodthat.adapter.ExercisesAdapter
import tk.atna.wodthat.databinding.FragmentExercisesBinding
import tk.atna.wodthat.internal.FirstLastItemDecoration
import tk.atna.wodthat.internal.ResultCode
import tk.atna.wodthat.internal.ExercisesViewModel
import tk.atna.wodthat.internal.setNonScrollableContentCallback
import tk.atna.wodthat.model.Exercise
import tk.atna.wodthat.stuff.Log
import tk.atna.wodthat.stuff.discoverVideoId
import tk.atna.wodthat.stuff.snackbar

class ExercisesFragment() : BottomNavFragment() {

    private val LAYOUT = R.layout.fragment_exercises

    private lateinit var binding: FragmentExercisesBinding

    private val viewModel: ExercisesViewModel by viewModels({ requireParentFragment() })

    private lateinit var adapter: ExercisesAdapter

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
        binding.rvExercises.adapter = null
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
            rvExercises.setHasFixedSize(true)
            rvExercises.adapter = adapter
            (rvExercises.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            rvExercises.addItemDecoration(
                FirstLastItemDecoration.create(requireContext(), DEFAULT_TOP_PADDING_DP)
            )
            // prevents toolbar scrolling for non-scrollable content (less than screen height)
            rvExercises.setNonScrollableContentCallback()
        }

    }

    private fun initAdapter() {
        adapter = ExercisesAdapter(requireContext(), object : ExercisesAdapter.ItemActionListener {
            override fun onItemClick(item: Exercise) {
                getNavController()?.navigate(
                    ExercisesFragmentDirections.actionExercisesToDetails(item.code, item.name)
                )
            }

            override fun onVideoClick(link: String) {
                getNavController()?.navigate(
                    ExercisesFragmentDirections.actionExercisesToVideo(
                        discoverVideoId(link)
                    )
                )
            }
        })
    }

    private fun pullData() {
        viewModel.getExercises().observe(this, Observer { resource ->

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