package tk.atna.wodthat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tk.atna.wodthat.R
import tk.atna.wodthat.databinding.ItemExerciseItemBinding
import tk.atna.wodthat.databinding.ItemTeaserItemBinding
import tk.atna.wodthat.internal.*
import tk.atna.wodthat.model.Exercise
import tk.atna.wodthat.stuff.discoverVideoId

class ExercisesAdapter private constructor(
    private val inflater: LayoutInflater,
    private val itemActionListener: ItemActionListener
) :
    ListAdapter<ExercisesAdapter.ExerciseItemWrapper, ExercisesAdapter.ItemViewHolder<ViewDataBinding>>(
        DiffCallback<ExerciseItemWrapper, ItemViewType>()
    ) {

    private var sourceItems: List<Exercise>? = null
    private var expandedItemId: String? = null

    private val internalActionListener: InternalActionListener

    constructor(
        context: Context,
        itemActionListener: ItemActionListener
    ) : this(LayoutInflater.from(context), itemActionListener)

    init {
        this.internalActionListener = object : InternalActionListener {
            override fun onItemClick(item: Exercise) {
                switchItemInternal(null)
                itemActionListener.onItemClick(item)
            }

            override fun onVideoClick(link: String) {
                itemActionListener.onVideoClick(link)
            }

            override fun onSwitchItem(itemId: String) {
                switchItem(itemId)
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
            ItemViewType.EXERCISE -> ExerciseItemViewHolder(inflater, parent, internalActionListener)
            ItemViewType.TEASER -> TeaserItemViewHolder(inflater, parent)
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

    override fun getItem(position: Int): ExerciseItemWrapper? {
        return if (position in 0 until itemCount)
            super.getItem(position)
        else
            null
    }

    fun setData(data: List<Exercise>) {
        sourceItems = data
        switchItemInternal(expandedItemId)
    }

    private fun switchItem(itemId: String) {
        switchItemInternal(
            // expand new item
            if (itemId != expandedItemId)
                itemId
            // collapse all items
            else
                null
        )
    }

    private fun switchItemInternal(itemId: String?) {
        val wrapped =
            sourceItems?.foldIndexed(mutableListOf<ExerciseItemWrapper>()) { index, dest, item ->
                val id = item.id.toString()
                val withDivider = index > 0
                // expand item
                if (id == itemId) {
                    // item itself
                    dest.add(
                        ExerciseItemWrapper(
                            viewType = ItemViewType.EXERCISE,
                            id = id,
                            item = item,
                            expanded = true,
                            withDivider = withDivider
                        )
                    )
                    // teaser
                    dest.add(
                        ExerciseItemWrapper(
                            viewType = ItemViewType.TEASER,
                            id = id,
                            item = item.teaser
                        )
                    )
                    return@foldIndexed dest

                // keep collapsed
                } else {
                    dest.add(
                        ExerciseItemWrapper(
                            viewType = ItemViewType.EXERCISE,
                            id = id,
                            item = item,
                            withDivider = withDivider
                        )
                    )
                    return@foldIndexed dest
                }
            }
        //
        expandedItemId = itemId
        submitList(wrapped)
    }


    interface ItemActionListener {

        fun onItemClick(item: Exercise)

        fun onVideoClick(link: String)

    }


    interface InternalActionListener {

        fun onItemClick(item: Exercise)

        fun onVideoClick(link: String)

        fun onSwitchItem(itemId: String)

    }


    class ExerciseItemWrapper(
        viewType: ItemViewType,
        id: String,
        item: Any? = null,
        val expanded: Boolean = false,
        val withDivider: Boolean = false
    ) :
        ItemWrapper<ItemViewType>(viewType, id, item) {

        override fun contentEquals(item: ItemWrapper<ItemViewType>?): Boolean {
            return super.contentEquals(item)
                    && item is ExerciseItemWrapper
                    && item.expanded == expanded
        }

    }


    enum class ItemViewType {

        EXERCISE,
        TEASER,
        ;
    }


    abstract class ItemViewHolder<out T> private constructor(
        val binding: T,
        var internalActionListener: InternalActionListener? = null
    ) :
        RecyclerView.ViewHolder(binding.root) where T : ViewDataBinding {

        constructor(
            inflater: LayoutInflater,
            @LayoutRes layout: Int,
            parent: ViewGroup,
            listener: InternalActionListener? = null
        ) : this(DataBindingUtil.inflate(inflater, layout, parent, false), listener)


        open fun onBindView(wrapper: ExerciseItemWrapper) {
            // override to use
        }

        open fun onViewRecycled() {
            // override to use
        }

    }


    class ExerciseItemViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        listener: InternalActionListener
    ) :
        ItemViewHolder<ItemExerciseItemBinding>(
            inflater,
            R.layout.item_exercise_item,
            parent,
            listener
        ) {

        override fun onBindView(wrapper: ExerciseItemWrapper) {
            val item = wrapper.item as Exercise
            //
            with(binding) {
                divider.isVisible = wrapper.withDivider
                tvName.text = item.name
                tvModality.text = item.modality
                // show level if there
                if (vgLevelGroup.visible(item.level.trim().isNotEmpty()))
                    tvLevel.text = item.level
                // show movement if there
                if (vgMovementGroup.visible(item.classExercise.trim().isNotEmpty()))
                    tvMovement.text = item.classExercise
                // show equipment if there
                if (vgEquipmentGroup.visible(item.equipment.trim().isNotEmpty()))
                    tvEquipment.text = item.equipment
                // launch exercise video
                val videoId = discoverVideoId(item.videoLink)
                if (btnVideo.enabled(videoId != null))
                    btnVideo.setOnClickListener { v ->
                        internalActionListener?.onVideoClick(item.videoLink)
                    }
                // expand teaser
                if (btnInfo.enabled(item.teaser.trim().isNotEmpty()))
                    btnInfo.setOnClickListener { v ->
//                        TextDialog.show(itemView.context, item.name, item.teaser, true, false)
                        internalActionListener?.onSwitchItem(item.id.toString())
                    }
            }
            // goto details
            itemView.setOnClickListener { v -> internalActionListener?.onItemClick(item) }
        }

    }


    class TeaserItemViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) :
        ItemViewHolder<ItemTeaserItemBinding>(
            inflater,
            R.layout.item_teaser_item,
            parent
        ) {

        override fun onBindView(wrapper: ExerciseItemWrapper) {
            val teaser = wrapper.item as String
            binding.tvTeaser.htmlText = teaser
        }

    }


}
