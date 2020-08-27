package tk.atna.wodthat.fragment

import android.os.Bundle
import android.view.View
import tk.atna.wodthat.internal.BottomNavigation
import tk.atna.wodthat.stuff.Log

abstract class BottomNavFragment : PrimaryFragment() {

    private var showBottomNav: Boolean? = null


    companion object {
        private const val SHOW_BOTTOM_NAV = "show_bottom_nav"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        if (savedInstanceState?.containsKey(SHOW_BOTTOM_NAV) == true)
            showBottomNav = savedInstanceState.getBoolean(SHOW_BOTTOM_NAV)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        initBottomNav()

//        Log.w("------------------ VIEW CREATED " + this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (showBottomNav != null)
            outState.putBoolean(SHOW_BOTTOM_NAV, showBottomNav!!)
    }

    protected open fun withBottomNav() = BottomNavigation.DEFAULT_NAV_STATE

    private fun initBottomNav() {
        val bottomNav = getBottomNavigation()
        bottomNav?.showBottomNavigation(
            withBottomNav()
                    && (if (showBottomNav == null) true else showBottomNav!!)
        )
    }

    protected fun showBottomNav(show: Boolean) {
        val bottomNav = getBottomNavigation()
        bottomNav?.let {
            if (showBottomNav == null
                || showBottomNav != show
            ) {
                showBottomNav = show
                bottomNav.showBottomNavigation(show)
            }
        }
    }

    private fun getBottomNavigation(): BottomNavigation? {
        val activity = requireActivity()
        try {
            return activity as BottomNavigation
            //
        } catch (e: ClassCastException) {
//            e.printStackTrace()
            Log.e("${e.message}")
            Log.e(activity.javaClass.simpleName + " not implementing " + BottomNavigation::class.simpleName)
        }
        //
        return null
    }

}