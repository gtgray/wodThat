package tk.atna.wodthat.internal

import android.view.View
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.MaterialToolbar


@BindingAdapter("visible")
fun visible(view: View, value: Boolean) {
    view.isVisible = value
}

@BindingAdapter("navigationIconColor")
fun setNavigationIconColor(view: MaterialToolbar, @ColorInt color: Int) {
    view.setNavigationIconColor(color)
}