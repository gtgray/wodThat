package tk.atna.wodthat.internal

import tk.atna.wodthat.R

interface BottomNavigation {

    companion object {
        const val DEFAULT_NAV_SIZE = R.dimen.bottom_navigation_size
        const val DEFAULT_NAV_STATE = true
        const val ACTION_SCROLL_UP = "action_scroll_up"
    }

    fun showBottomNavigation(show: Boolean)

}
