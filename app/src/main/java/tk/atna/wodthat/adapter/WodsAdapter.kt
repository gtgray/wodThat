package tk.atna.wodthat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import tk.atna.wodthat.R
import tk.atna.wodthat.databinding.ItemWodItemBinding
import tk.atna.wodthat.internal.DiffCallback
import tk.atna.wodthat.internal.ItemViewHolder
import tk.atna.wodthat.internal.ItemWrapper
import tk.atna.wodthat.internal.visible
import tk.atna.wodthat.model.Wod
import tk.atna.wodthat.model.addTextLine
import tk.atna.wodthat.model.addWodExercises
import tk.atna.wodthat.model.formatHeadline
import tk.atna.wodthat.stuff.TimestampUtils


class WodsAdapter private constructor(
    private val inflater: LayoutInflater,
    private val itemActionListener: ItemActionListener
) :
    ListAdapter<WodsAdapter.WodItemWrapper, ItemViewHolder<ViewDataBinding, WodsAdapter.InternalActionListener, WodsAdapter.WodItemWrapper>>(
        DiffCallback<WodItemWrapper, ItemViewType>()
    ) {

    private val internalActionListener: InternalActionListener

    constructor(
        context: Context,
        itemActionListener: ItemActionListener
    ) : this(LayoutInflater.from(context), itemActionListener)

    init {
        this.internalActionListener = object : InternalActionListener {
            override fun onItemClick(item: Wod) {
                itemActionListener.onItemClick(item)
            }

            override fun onSaveClick(item: Wod) {
                itemActionListener.onSaveClick(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.viewType?.ordinal ?: -1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ViewDataBinding, InternalActionListener, WodItemWrapper> {
        return when (ItemViewType.values()[viewType]) {
            ItemViewType.WOD -> WodItemViewHolder(inflater, parent, internalActionListener)
        }
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder<ViewDataBinding, InternalActionListener, WodItemWrapper>,
        position: Int
    ) {
        val item = getItem(position)
        holder.onBindView(item!!)
    }

    override fun onViewRecycled(
        holder: ItemViewHolder<ViewDataBinding, InternalActionListener, WodItemWrapper>
    ) {
        super.onViewRecycled(holder)
        //
        holder.onViewRecycled()
    }

    override fun getItem(position: Int): WodItemWrapper? {
        return if (position in 0 until itemCount)
            super.getItem(position)
        else
            null
    }

    fun setData(data: List<Wod>) {
        val wrapped = data.mapIndexed { index, item ->
            WodItemWrapper(
                viewType = ItemViewType.WOD,
                id = item.id.toString(),
                item = item,
                withDivider = index > 0
            )
        }
        //
        submitList(wrapped)
    }


    interface ItemActionListener {

        fun onItemClick(item: Wod)

        fun onSaveClick(item: Wod)

    }


    interface InternalActionListener {

        fun onItemClick(item: Wod)

        fun onSaveClick(item: Wod)

    }


    class WodItemWrapper(
        viewType: ItemViewType,
        id: String,
        item: Any? = null,
        val withDivider: Boolean = false
    ) :
        ItemWrapper<ItemViewType>(viewType, id, item) {

        override fun contentEquals(item: ItemWrapper<ItemViewType>?): Boolean {
            return super.contentEquals(item)
                    && item is WodItemWrapper
        }

    }


    enum class ItemViewType {

        WOD,
        ;
    }


    class WodItemViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        listener: InternalActionListener
    ) :
        ItemViewHolder<ItemWodItemBinding, InternalActionListener, WodItemWrapper>(
            inflater,
            R.layout.item_wod_item,
            parent,
            listener
        ) {

        override fun onBindView(wrapper: WodItemWrapper) {
            val item = wrapper.item as Wod
            //
            with(binding) {
                divider.isVisible = wrapper.withDivider
                tvName.text = item.name
                tvHeadline.text = formatHeadline(item.headline)

                // add rating if there
                if (tvRating.visible(item.summaryRating > 0))
                    tvRating.text = String.format("%s/10", item.summaryRating)

                tvModality.text = item.modality
                tvTags.text = item.tagsInternal.replace(",", "\n")
                done = item.done

                saved = item.saved
                btnSave.setOnClickListener { v ->
                    internalActionListener?.onSaveClick(item)
                }

                // clear exercises list
                vgWodItems.removeAllViews()
                // add general repetitions scheme if there
                item.generalScheme?.let {
                    addTextLine(vgWodItems, item.generalScheme, R.dimen.text_14)
                }
                // add exercises themself
                addWodExercises(vgWodItems, item.exercises, R.dimen.text_14)

                // show duration if there
                if (vgDurationGroup.visible(item.duration > 0))
                    tvDuration.text = TimestampUtils.secsToDuration(item.duration)

                // show results count if there
                if (vgResultsCountGroup.visible(item.resultsCount > 0))
                    tvResultsCount.text = item.resultsCount.toString()

                // show comments count if there
                if (vgCommentsCountGroup.visible(item.commentsCount > 0))
                    tvCommentsCount.text = item.commentsCount.toString()

            }
            // goto details
            itemView.setOnClickListener { v -> internalActionListener?.onItemClick(item) }
        }

    }


}
