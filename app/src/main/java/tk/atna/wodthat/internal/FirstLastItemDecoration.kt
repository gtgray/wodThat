package tk.atna.wodthat.internal

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tk.atna.wodthat.stuff.dpToPixels

class FirstLastItemDecoration private constructor(
    private val paddingFirst: Int,
    private val paddingLast: Int
) : RecyclerView.ItemDecoration() {

    private var orientation: Int = -1 // one time change parameter

    companion object {

        fun create(context: Context, paddingFirstDp: Int) =
            create(context, paddingFirstDp, 0)

        fun create(context: Context, paddingFirstDp: Int, paddingLastDp: Int) =
            FirstLastItemDecoration(
                dpToPixels(context, paddingFirstDp),
                dpToPixels(context, paddingLastDp)
            )
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        //
        if (paddingFirst > 0
            && parent.getChildAdapterPosition(view) == 0
        ) {
            if (getOrientation(parent) == LinearLayoutManager.VERTICAL)
                outRect.top = paddingFirst
            else
                outRect.left = paddingFirst
        }
        //
        if (paddingLast > 0
            && parent.getChildAdapterPosition(view) == getTotalItemCount(parent)?.minus(1)
        ) {
            if (getOrientation(parent) == LinearLayoutManager.VERTICAL)
                outRect.bottom = paddingLast
            else
                outRect.right = paddingLast
        }
    }

    private fun getTotalItemCount(parent: RecyclerView) = parent.adapter?.itemCount

    private fun getOrientation(parent: RecyclerView): Int {
        if (orientation == -1) {
            if (parent.layoutManager is LinearLayoutManager)
                orientation = (parent.layoutManager as LinearLayoutManager).orientation
            else
                throw IllegalStateException(this.javaClass.name + " supports LinearLayoutManager only")
        }
        //
        return orientation
    }

}