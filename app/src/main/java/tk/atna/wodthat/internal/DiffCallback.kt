package tk.atna.wodthat.internal

import androidx.recyclerview.widget.DiffUtil

class DiffCallback<T, V>() : DiffUtil.ItemCallback<T>() where T : ItemWrapper<V> {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.itemEquals(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.contentEquals(newItem)
    }

}