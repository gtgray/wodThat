package tk.atna.wodthat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import tk.atna.wodthat.R
import tk.atna.wodthat.databinding.FragmentListBinding

class ListFragment() : BottomNavFragment() {

    private val LAYOUT = R.layout.fragment_list

    private lateinit var binding: FragmentListBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false)
        binding.clickHandler = this
        return binding.root
    }

    fun onGotoData() {
        getNavController()?.navigate(ListFragmentDirections.actionListToData())
    }

}