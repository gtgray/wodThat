package tk.atna.wodthat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

class NonTouchableCoordinatorLayout : CoordinatorLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

}