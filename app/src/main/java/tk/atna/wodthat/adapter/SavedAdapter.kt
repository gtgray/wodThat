package tk.atna.wodthat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tk.atna.wodthat.R
import tk.atna.wodthat.databinding.ItemSavedEmptyBinding
import tk.atna.wodthat.databinding.ItemSavedItemBinding
import tk.atna.wodthat.internal.DiffCallback
import tk.atna.wodthat.internal.ItemWrapper
import tk.atna.wodthat.model.Wod

class SavedAdapter private constructor(
    private val inflater: LayoutInflater,
    private val itemActionListener: ItemActionListener
) :
    ListAdapter<SavedAdapter.SavedItemWrapper, SavedAdapter.ItemViewHolder<ViewDataBinding>>(
        DiffCallback<SavedItemWrapper, ItemViewType>()
    ) {

    private val internalActionListener: InternalActionListener

    constructor(
        context: Context,
        itemActionListener: ItemActionListener
    ) : this(LayoutInflater.from(context), itemActionListener)

    init {
        this.internalActionListener = object : InternalActionListener {

            override fun onItemClick(item: SavedItemWrapper) {
                itemActionListener.onItemClick(item)
            }

            override fun onRemove(item: SavedItemWrapper) {
                itemActionListener.onRemove(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.viewType?.ordinal ?: -1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ViewDataBinding> {
        return when (ItemViewType.values()[viewType]) {
            ItemViewType.EMPTY -> EmptyItemViewHolder.create(inflater, parent)
            ItemViewType.ITEM -> SavedItemViewHolder.create(inflater, parent, internalActionListener)
            else -> throw IllegalArgumentException("Unknown item view type")
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder<ViewDataBinding>, position: Int) {
        val item = getItem(position)
        holder.onBindView(item!!)
    }

    override fun onViewRecycled(holder: ItemViewHolder<ViewDataBinding>) {
        super.onViewRecycled(holder)
        //
        holder.onViewRecycled()
    }

    override fun getItem(position: Int): SavedItemWrapper? {
        return if (position in 0 until itemCount)
            super.getItem(position)
        else
            null
    }

    fun setData(data: List<Wod>?) {
        val wrapped = if (!data.isNullOrEmpty()) {
            data.map { item ->
                SavedItemWrapper(
                    viewType = ItemViewType.ITEM,
                    id = item.name,
                    item = item
                )
            }
        // otherwise, show empty view
        } else
            arrayListOf(
                SavedItemWrapper(
                    viewType = ItemViewType.EMPTY,
                    id = ItemViewType.EMPTY.toString()
                )
            )
        //
        submitList(wrapped)
    }


    interface ItemActionListener {

        fun onItemClick(item: SavedItemWrapper)

        fun onRemove(item: SavedItemWrapper)

    }


    interface InternalActionListener {

        fun onItemClick(item: SavedItemWrapper)

        fun onRemove(item: SavedItemWrapper)

    }


    class SavedItemWrapper(
        viewType: ItemViewType,
        id: String,
        item: Any? = null
    ) : ItemWrapper<ItemViewType>(viewType, id, item) {
        // nothing here
    }


    enum class ItemViewType {

        EMPTY,
        ITEM,
        ;
    }


    abstract class ItemViewHolder<out T>(
        val binding: T,
        var internalActionListener: InternalActionListener? = null
    ) : RecyclerView.ViewHolder(binding.root) where T : ViewDataBinding {

        companion object {
            fun <T : ViewDataBinding> inflate(
                inflater: LayoutInflater,
                @LayoutRes layout: Int,
                parent: ViewGroup
            ): T {
                return DataBindingUtil.inflate(inflater, layout, parent, false)
            }
        }

        open fun onBindView(wrapper: SavedItemWrapper) {
            // override to use
        }

        open fun onViewRecycled() {
            // override to use
        }

    }


    class EmptyItemViewHolder(binding: ItemSavedEmptyBinding) :
        ItemViewHolder<ItemSavedEmptyBinding>(binding) {

        companion object {
            @LayoutRes
            const val LAYOUT = R.layout.item_saved_empty

            fun create(inflater: LayoutInflater, parent: ViewGroup): EmptyItemViewHolder {
                return EmptyItemViewHolder(inflate(inflater, LAYOUT, parent))
            }
        }

    }


    class SavedItemViewHolder(binding: ItemSavedItemBinding) :
        ItemViewHolder<ItemSavedItemBinding>(binding) {

        companion object {
            @LayoutRes
            const val LAYOUT = R.layout.item_saved_item

            fun create(
                inflater: LayoutInflater,
                parent: ViewGroup,
                listener: InternalActionListener
            ): SavedItemViewHolder {
                return SavedItemViewHolder(inflate(inflater, LAYOUT, parent))
                    .apply { internalActionListener = listener }
            }
        }

        override fun onBindView(wrapper: SavedItemWrapper) {
            val item = wrapper.item as Wod

            // todo: implement here!!!

            binding.tvName.text = item.name

//            itemView.setOnClickListener { v -> internalActionListener?.onItemClick(item) }
        }

    }


}
