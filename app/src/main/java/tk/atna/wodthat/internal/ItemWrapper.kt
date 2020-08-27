package tk.atna.wodthat.internal

open class ItemWrapper<T>(
    val viewType: T,
    val id: String,
    val item: Any?
) {

//    public static <T> ItemWrapper<T> copy(ItemWrapper<T> source) {
//        if(source != null)
//            return source.copy()
//        //
//        return null
//    }

//    public ItemWrapper<T> copy() {
//        return new ItemWrapper<T>()
//                .setId(id)
//                .setViewType(viewType)
//                .setItem(item)
//    }

    fun itemEquals(item: ItemWrapper<T>?): Boolean =
        this.id == item?.id
                && this.viewType == item.viewType

    open fun contentEquals(item: ItemWrapper<T>?): Boolean =
        this.item == item?.item

}
