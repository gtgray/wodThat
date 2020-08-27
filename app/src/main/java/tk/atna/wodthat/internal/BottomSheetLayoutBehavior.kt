package tk.atna.wodthat.internal

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.children
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import tk.atna.wodthat.R

class BottomSheetLayoutBehavior : BottomSheetBehavior<ViewGroup> {

    private var nestedAppBarLayout: AppBarLayout? = null
    @ViewCompat.NestedScrollType private var lastStartedType: Int? = null

    private var backKeyListener: View.OnKeyListener? = null
    private var outside: View? = null

    private var shift: Int = 0
    private var shifted: Boolean = false


    constructor() : super()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetLayoutBehavior)
        shift =
            a.getDimension(R.styleable.BottomSheetLayoutBehavior_behavior_shiftHeight, 0f).toInt()
        a.recycle()
    }

    companion object {
        fun from(view: ViewGroup): BottomSheetLayoutBehavior {
            val params = view.layoutParams as CoordinatorLayout.LayoutParams
            return params.behavior as BottomSheetLayoutBehavior
        }
    }

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        sheet: ViewGroup,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        // let's measure bottom sheet
        val parentHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec)
        val maxSheetHeight = parentHeight + shift
        sheet.measure(
            parentWidthMeasureSpec,
            View.MeasureSpec.makeMeasureSpec(maxSheetHeight, View.MeasureSpec.AT_MOST)
        )

//        Log.w("--------------- MEASURE $maxSheetHeight - ${sheet.measuredHeight}")

        // do it just once
        if (!shifted
            // sheet was measured with max height
            && sheet.measuredHeight == maxSheetHeight) {
            // add negative vertical shift to sheet
            sheet.y = sheet.y - shift
            // correct peek height
            peekHeight -= shift
            // remember
            shifted = true
        }

        // attempt to find appbar layout in bottom sheet (layout with this behavior)
        nestedAppBarLayout ?: discoverNestedAppBarLayout(sheet)

        // add back key listener to collapse bottom sheet on back button pressed
        backKeyListener ?: provideBackKeyListener(parent)
        //
        outside ?: provideListenableOutside(parent, sheet)

        // consume children measuring
        return true
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        sheet: ViewGroup,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        lastStartedType = type
        //
        return super.onStartNestedScroll(
            coordinatorLayout,
            sheet,
            directTargetChild,
            target,
            axes,
            type
        )
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        sheet: ViewGroup,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, sheet, target, dx, dy, consumed, type)
        //
        if (nestedAppBarLayout?.isLiftOnScroll == true) {
            nestedAppBarLayout!!.setLifted(shouldLift(target, sheet))
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        sheet: ViewGroup,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, sheet, target, type)
        //
        if (lastStartedType == ViewCompat.TYPE_TOUCH || type == ViewCompat.TYPE_NON_TOUCH) {
            if (nestedAppBarLayout?.isLiftOnScroll == true) {
                nestedAppBarLayout!!.setLifted(shouldLift(target, sheet))
            }
        }
    }

    private fun shouldLift(defaultScrollingView: View?, sheet: ViewGroup): Boolean {
        val liftOnScrollTargetViewId = nestedAppBarLayout!!.liftOnScrollTargetViewId
        val scrollingView =
            if (liftOnScrollTargetViewId != View.NO_ID)
                sheet.findViewById(liftOnScrollTargetViewId)
            else
                defaultScrollingView
        //
        return scrollingView != null
                && (scrollingView.canScrollVertically(-1) || scrollingView.scrollY > 0)
    }

    private fun discoverNestedAppBarLayout(parent: ViewGroup) {
        nestedAppBarLayout = parent.children.find { v -> v is AppBarLayout } as AppBarLayout
    }

    private fun provideBackKeyListener(parent: CoordinatorLayout) {
        backKeyListener = object: View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if(event?.keyCode == KeyEvent.KEYCODE_BACK
                    && state == STATE_EXPANDED) {
                    // special case for the back key, we do not even try to send it
                    // to the drop down list but instead, consume it immediately
                    if(event.action == KeyEvent.ACTION_DOWN
                        && event.repeatCount == 0) {
                        v?.keyDispatcherState?.startTracking(event, this)
                        return true
                        //
                    } else if(event.action == KeyEvent.ACTION_UP) {
                        v?.keyDispatcherState?.handleUpEvent(event)
                        //
                        if(event.isTracking
                            && !event.isCanceled) {
                            state = STATE_COLLAPSED
                            return true
                        }
                    }
                }
                //
                return false
            }
        }
        //
        parent.isFocusableInTouchMode = true
        parent.requestFocus()
        parent.setOnKeyListener(backKeyListener)
    }

    private fun provideListenableOutside(
        parent: CoordinatorLayout,
        sheet: ViewGroup
    ) {
        // create outside view
        outside = View(parent.context)
        outside!!.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        // discover outside view place in view hierarchy
        val index = parent.children.indexOf(sheet)
        // insert outside view below bottom sheet layout and above other views
        parent.addView(outside, index)
        //
        provideBottomSheetCallback()
    }

    private fun provideBottomSheetCallback() {
        addBottomSheetCallback(object: BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    STATE_EXPANDED -> {
                        outside!!.setOnClickListener {
                            state = STATE_COLLAPSED
                        }
                    }
                    //
                    else -> {
                        outside!!.setOnClickListener(null)
                        outside!!.isClickable = false
                    }
                }
            }

            override fun onSlide(sheet: View, slideOffset: Float) { }
        })
    }


}