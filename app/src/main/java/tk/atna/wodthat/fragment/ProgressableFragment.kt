package tk.atna.wodthat.fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment

abstract class ProgressableFragment() : Fragment() {

    private val handler: Handler = Handler()
    private var startTime: Long = -1L
    private var postedHide = false
    private var postedShow = false
    private var dismissed = false

    //
    private val delayedHide = Runnable {
        postedHide = false
        startTime = -1L
        hideProgressInternal()
    }

    //
    private val delayedShow = Runnable {
        postedShow = false
        if (!dismissed) {
            startTime = System.currentTimeMillis()
            showProgressInternal()
        }
    }

    companion object {
        private const val MIN_SHOW_TIME = 500L // ms
        private const val MIN_DELAY = 500L // ms
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        removeCallbacks()
    }

    override fun onDestroy() {
        super.onDestroy()
        //
        removeCallbacks()
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    @Synchronized
    protected fun showProgress() {
        // Reset the start time.
        startTime = -1
        dismissed = false
        handler.removeCallbacks(delayedHide)
        postedHide = false
        if (!postedShow) {
            handler.postDelayed(delayedShow, MIN_DELAY)
            postedShow = true
        }
    }

    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    @Synchronized
    protected fun hideProgress() {
        dismissed = true
        handler.removeCallbacks(delayedShow)
        postedShow = false
        val diff = System.currentTimeMillis() - startTime
        when {
            // The progress spinner has been shown long enough
            // OR was not shown yet. If it wasn't shown yet,
            // it will just never be shown.
            (diff >= MIN_SHOW_TIME || startTime == -1L) -> hideProgressInternal()
            // The progress spinner is shown, but not long enough,
            // so put a delayed message in to hide it when its been
            // shown long enough.
            (!postedHide) -> {
                handler.postDelayed(delayedHide, MIN_SHOW_TIME - diff)
                postedHide = true
            }
        }
    }

    private fun removeCallbacks() {
        handler.removeCallbacks(delayedHide)
        handler.removeCallbacks(delayedShow)
    }

    protected abstract fun showProgressInternal()

    protected abstract fun hideProgressInternal()

}
