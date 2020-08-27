package tk.atna.wodthat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import tk.atna.wodthat.R
import tk.atna.wodthat.databinding.ItemResultItemBinding
import tk.atna.wodthat.databinding.ItemWodItemBinding
import tk.atna.wodthat.internal.DiffCallback
import tk.atna.wodthat.internal.ItemViewHolder
import tk.atna.wodthat.internal.ItemWrapper
import tk.atna.wodthat.internal.visible
import tk.atna.wodthat.model.*
import tk.atna.wodthat.stuff.*


class WodResultsAdapter private constructor(
    private val inflater: LayoutInflater,
    private val itemActionListener: ItemActionListener
) :
    ListAdapter<WodResultsAdapter.ResultItemWrapper, ItemViewHolder<ViewDataBinding, WodResultsAdapter.InternalActionListener, WodResultsAdapter.ResultItemWrapper>>(
        DiffCallback<ResultItemWrapper, ItemViewType>()
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
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.viewType?.ordinal ?: -1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ViewDataBinding, InternalActionListener, ResultItemWrapper> {
        return when (ItemViewType.values()[viewType]) {
            ItemViewType.RESULT -> ResultItemViewHolder(inflater, parent, internalActionListener)
        }
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder<ViewDataBinding, InternalActionListener, ResultItemWrapper>,
        position: Int
    ) {
        val item = getItem(position)
        holder.onBindView(item!!)
    }

    override fun onViewRecycled(
        holder: ItemViewHolder<ViewDataBinding, InternalActionListener, ResultItemWrapper>
    ) {
        super.onViewRecycled(holder)
        //
        holder.onViewRecycled()
    }

    override fun getItem(position: Int): ResultItemWrapper? {
        return if (position in 0 until itemCount)
            super.getItem(position)
        else
            null
    }

    fun setData(data: List<WodResult>) {
        val wrapped = data.mapIndexed { index, item ->
            ResultItemWrapper(
                viewType = ItemViewType.RESULT,
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

    }


    interface InternalActionListener {

        fun onItemClick(item: Wod)

    }


    class ResultItemWrapper(
        viewType: ItemViewType,
        id: String,
        item: Any? = null,
        val withDivider: Boolean = false
    ) :
        ItemWrapper<ItemViewType>(viewType, id, item) {

        override fun contentEquals(item: ItemWrapper<ItemViewType>?): Boolean {
            return super.contentEquals(item)
                    && item is ResultItemWrapper
        }

    }


    enum class ItemViewType {

        RESULT,
        ;
    }


    class ResultItemViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        listener: InternalActionListener
    ) :
        ItemViewHolder<ItemResultItemBinding, InternalActionListener, ResultItemWrapper>(
            inflater,
            R.layout.item_result_item,
            parent,
            listener
        ) {

        override fun onBindView(wrapper: ResultItemWrapper) {
            val item = wrapper.item as WodResult
            //
            with(binding) {
                divider.isVisible = wrapper.withDivider

                if (item.author.avatarLink != null)
                    placeImage(ivUser, item.author.avatarLink)
                else
                    placeGeneratedAvatar(ivUser, item.author.fullName, 40)

                if (ivFlag.visible(item.country?.flagLink != null))
                    placeImage(ivFlag, item.country?.flagLink)

                if (ivGender.visible(item.author.gender != null))
                    placeGender(ivGender, item.author.gender!!)

                tvUser.text = item.author.fullName
                tvDate.text = TimestampUtils.millisToLocalDate(item.completedAt.time)
                tvDuration.text = TimestampUtils.secsToDuration(item.seconds)
                tvReps.text = discoverReps(item)

                if (tvRank.visible(item.rank > 0))
                    placeRank(tvRank, item.rank)

                tvRxed.visible(item.rxd)

            }
            // goto details
//            itemView.setOnClickListener { v -> internalActionListener?.onItemClick(item) }
        }

    }


}
