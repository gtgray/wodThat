package tk.atna.wodthat.internal

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import tk.atna.wodthat.R
import tk.atna.wodthat.stuff.getDimen


fun View.visible(visible: Boolean): Boolean {
    isVisible = visible
    return visible
}

fun View.enabled(enabled: Boolean): Boolean {
    isEnabled = enabled
    return enabled
}

fun View.backgroundTint(@ColorRes colorRes: Int) {
    ViewCompat.setBackgroundTintList(this, ContextCompat.getColorStateList(context, colorRes))
}


inline var TextView.htmlText: CharSequence
    get() = text
    set(value) {
        text = HtmlCompat.fromHtml(value.toString(), HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

fun TextView.textSize(@DimenRes sizeRes: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_DIP, getDimen(context, sizeRes))
}

// prevents toolbar scrolling for non-scrollable content (less than screen height)
fun RecyclerView.setNonScrollableContentCallback(): RecyclerView.ChildDrawingOrderCallback {
    val callback = RecyclerView.ChildDrawingOrderCallback { childCount, i ->
        if (i == childCount - 1)
            adjustNestedScrollingState()
        //
        return@ChildDrawingOrderCallback i
    }
    setChildDrawingOrderCallback(callback)
    //
    return callback
}

private fun RecyclerView.adjustNestedScrollingState() {

//    Log.w("----------------- CAN SCROLL ${canScrollVertically(-1)}, ${canScrollVertically(1)}")

    val newState = canScrollVertically(-1) || canScrollVertically(1)
    // only update if changed
    if (newState != isNestedScrollingEnabled) {
// todo: need nested scroll down to prevent toolbar stuck above the screen top edge
//       when items removed and nested scroll should be disabled.
//       scrolling doesn't work during items animations (add, remove etc.)
//       Better way to here is to wait all animations end
//       (see https://stackoverflow.com/a/46829473/4453154)
        //
//            if (!newState) {
//                recyclerView.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
//                recyclerView.smoothScrollBy(0, 100)
//                recyclerView.stopNestedScroll()
//            }

        isNestedScrollingEnabled = newState

//            Log.w("----------------- NESTED ${recyclerView.isNestedScrollingEnabled}")

    }
}

fun MaterialToolbar.setNavigationIconColor(@ColorInt color: Int) {
    navigationIcon?.mutate()?.setTint(color)
}

fun ChipGroup.addIconedChips(iconRes: Int, captions: String) {
    captions.split(",")
        .filter { item -> item.trim().isNotEmpty() }
        .forEach { item ->
            val chip = Chip(context, null, R.attr.chipIconedStyle)
            chip.setEnsureMinTouchTargetSize(false)
            chip.text = item
            chip.setChipIconResource(iconRes)
            chip.setChipIconTintResource(R.color.clr_accent)
            addView(chip)
        }
}

fun ChipGroup.addChips(captions: String) {
    captions.split(",")
        .filter { item -> item.trim().isNotEmpty() }
        .forEach { item ->
            val chip = Chip(context, null, R.attr.chipSimpleStyle)
            chip.setEnsureMinTouchTargetSize(false)
            chip.text = item
            addView(chip)
        }
}

