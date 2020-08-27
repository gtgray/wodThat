package tk.atna.wodthat.internal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class ItemViewHolder<out B, L, W> private constructor(
    val binding: B,
    var internalActionListener: L? = null
) :
    RecyclerView.ViewHolder(binding.root) where B : ViewDataBinding, W : ItemWrapper<out Any> {

    constructor(
        inflater: LayoutInflater,
        @LayoutRes layout: Int,
        parent: ViewGroup,
        listener: L? = null
    ) : this(DataBindingUtil.inflate(inflater, layout, parent, false), listener)


    open fun onBindView(wrapper: W) {
        // override to use
    }

    open fun onViewRecycled() {
        // override to use
    }

}