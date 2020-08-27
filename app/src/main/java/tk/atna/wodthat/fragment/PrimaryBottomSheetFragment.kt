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

open class PrimaryBottomSheetFragment() : BottomSheetDialogFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //
        arguments?.let {
            onProcessArguments(requireArguments())
        }
    }

    protected open fun onProcessArguments(data: Bundle) {
        // override to use
        // todo: notice! children that could be further extended
        // todo: must override this method with annotation @CallSuper
    }

//    override fun showProgressInternal() {
//        // override to use
//    }

//    override fun hideProgressInternal() {
//        // override to use
//    }

}