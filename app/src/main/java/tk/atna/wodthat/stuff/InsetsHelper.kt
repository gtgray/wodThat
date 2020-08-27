package tk.atna.wodthat.stuff

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object InsetsHelper {
// for more info about discovering actual status bar and system nav bar sizes
// https://photos.app.goo.gl/E09HOVX5duuFHXes1

    private val STATUS_BAR_HEIGHT = MutableLiveData<Int>()
    private val NAVIGATION_BAR_HEIGHT = MutableLiveData<Int>()


    fun discoverSystemInsets(view: View?) {
        view?.setOnApplyWindowInsetsListener { v, insets ->

            // only set one time
//                if(mStatusBarHeight.getValue() == null)
            STATUS_BAR_HEIGHT.postValue(insets.systemWindowInsetTop)
            //
            // todo: check, how it works with soft input
            NAVIGATION_BAR_HEIGHT.postValue(insets.systemWindowInsetBottom)

//                Log.w("--------------- ON APPLY INSETS " + insets)

            v.onApplyWindowInsets(insets)
        }
    }

    fun pullStatusBarHeight(): LiveData<Int> = STATUS_BAR_HEIGHT

    fun pullNavigationBarHeight(): LiveData<Int> = NAVIGATION_BAR_HEIGHT

    private fun getStatusBarHeightFromDimen(context: Context?): Int {
        var result = 0
        if (context != null) {
            val resourceId = context.resources
                .getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0)
                result = context.resources.getDimensionPixelSize(resourceId)
        }
        //
        return result
    }

    private fun getNavigationBarHeightFromDimen(context: Context?): Int {
        var result = 0
        if (context != null) {
            val resourceId = context.resources
                .getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0)
                result = context.resources.getDimensionPixelSize(resourceId)
        }
        //
        return result
    }


}