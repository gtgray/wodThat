package tk.atna.wodthat.internal

import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import tk.atna.wodthat.R
import tk.atna.wodthat.fragment.BottomNavFragment
import tk.atna.wodthat.stuff.Log
import java.util.*

class MainNavController(
    private val fragmentManager: FragmentManager,
    @IdRes private val containerId: Int
) {

    private var backStack: LinkedList<NavItem> = LinkedList()
    private var navigatedListener: OnNavigatedListener? = null

    companion object {
        private const val BACK_STACK = "back_stack"
    }

    fun setNavigatedListener(listener: OnNavigatedListener): MainNavController {
        navigatedListener = listener
        return this
    }

    fun saveState(): Bundle {
        val state = Bundle()
        state.putSerializable(BACK_STACK, backStack)
        return state
    }

    @Suppress("UNCHECKED_CAST")
    fun restoreState(state: Bundle?) {
        state?.let {
            if (state.containsKey(BACK_STACK))
                backStack = LinkedList(state.getSerializable(BACK_STACK) as List<NavItem>)
        }
    }

    fun getCurrentItem(): NavItem? = backStack.peekLast()

    fun startNavigation() {
        when (backStack.isEmpty()) {
            true -> navigate(NavItem.discoverPrimary())
            false -> navigate(getCurrentItem())
        }
    }

    fun navigate(destItem: NavItem?) =
        destItem?.let {
            navigate(destItem, null)
        }

    fun navigate(destItem: NavItem, animOptions: AnimOptions?) {
        val primary = destItem.primary
        // attempt to start with non-primary item
        // or navigate primary later
        if (backStack.size == 0 && !primary)
            throw IllegalStateException("Use navigatePrimary() method to start with primary item")

        val currItem = getCurrentItem()

        // attempt to navigate same item one after another
        if (destItem == currItem)
            return

        // need to reorder back stack (lift target dest up)
        val destIndex = backStack.lastIndexOf(destItem)
        if (destIndex != -1) {
            if (!primary) {
                backStack.removeAt(destIndex)
                // control primary items duplication in stack
                if (destIndex > 0
                    // compare new item at removed index with item with previous index
                    && backStack[destIndex].primary
                    && backStack[destIndex - 1].primary
                )
                    backStack.removeAt(destIndex)
            }
        }
        // update back stack
        backStack.add(destItem)
        // do navigate to destination
        placeFragment(destItem, currItem/*, null*/)
        // notify listener
        dispatchNavigated(destItem)

//        Handler().postDelayed({
//            dumpBackStack(fragmentManager)
//        }, 500)

    }

    fun onBackHandled(): Boolean {
        // internal fragment navigation
        if ((fragmentManager.primaryNavigationFragment as NavHostFragment).navController.popBackStack())
            return true

        // primary item remained
        if (backStack.size <= 1)
            return false

        // navigate back stack
        val currItem = backStack.removeLast()
        val destItem = backStack.last
        placeFragment(destItem, currItem)
        // notify listener
        dispatchNavigated(destItem)
        //
        return true
    }

    fun navigateReselected(navItem: NavItem?) {
        navItem?.let {
// just take current navHostFragment, reselection is usually made for current app section (bottom nav menu item)
//            val navHostFragment = fragmentManager.primaryNavigationFragment as NavHostFragment
            val navHostFragment =
                fragmentManager.findFragmentByTag(navItem.getTag()) as NavHostFragment
            val navController = navHostFragment.navController
            val startDest = navController.graph.startDestination
            // pop back stack
//            popUntilStart(navState.navController, startDest)
            navController.popBackStack(startDest, false)
            // force started dest to auto scroll after 2rd reselect
            val currentDest = navController.currentDestination?.id
            if (startDest == currentDest) {
                val fragment = navHostFragment.childFragmentManager.fragments[0]
                val data = Bundle()
                data.putBoolean(BottomNavigation.ACTION_SCROLL_UP, true)
                (fragment as BottomNavFragment).updateArguments(data)
//                    // 1st reselect - just pop until start
//                    } else {
//                        navController.popBackStack(startDest, false)
//
//                        Log.i("--------------------- POP BACK STACK ")
            }
        }
    }

    private fun placeFragment(nextItem: NavItem, prevItem: NavItem?) {
        val animOptions = AnimOptions(
            enterAnim = R.anim.nav_default_enter_anim,
            exitAnim = R.anim.nav_default_exit_anim
        )
        //
        placeFragment(nextItem, prevItem, animOptions)
    }

    private fun placeFragment(nextItem: NavItem, prevItem: NavItem?, animOptions: AnimOptions?) {
        val prevFragment = prevItem?.let { createOrRestoreNavHostFragment(prevItem) }
        val nextFragment = createOrRestoreNavHostFragment(nextItem)
        val ft = fragmentManager.beginTransaction()

        // add transit animations if there
        if (animOptions != null)
            ft.setCustomAnimations(animOptions)

        // detach previous if there
        if (prevFragment != null) {
            ft.detach(prevFragment)
        }

        // need re-attach
        if (nextFragment.isDetached) {
            ft.attach(nextFragment)
            // just created
        } else
            ft.add(containerId, nextFragment, nextItem.getTag())

        // finish transaction
        ft.setPrimaryNavigationFragment(nextFragment)
//        ft.setReorderingAllowed(true)
        ft.commitNow()

        // reset the graph if the currentDestination is not valid
        // (happens when the back stack is popped after using the back button)
        val navController = nextFragment.navController
        if (navController.currentDestination == null) {
            navController.navigate(navController.graph.id)
        }

    }

    private fun createOrRestoreNavHostFragment(navItem: NavItem): NavHostFragment {
        var navHostFragment =
            fragmentManager.findFragmentByTag(navItem.getTag()) as NavHostFragment?
        // already created, restore it
        navHostFragment?.let { return navHostFragment!! }
        // no such instance, create it
        navHostFragment = NavHostFragment.create(navItem.navGraphRes)
        navHostFragment.retainInstance = true
        return navHostFragment
    }

    private fun dispatchNavigated(destItem: NavItem?) {
        destItem?.let {
            navigatedListener?.onNavigated(destItem)
        }
    }

    private fun dumpBackStack(fm: FragmentManager) {

//        Log.i("------------------ NAVIGATE (" + backStack.size() + ") " + fm.getFragments())
        Log.i("------------------ NAVIGATE entries $backStack")

//        LogWriter logw = new LogWriter(MainActivity.class.getSimpleName())
//        PrintWriter pw = new PrintWriter(logw)
//        fm.dump("  ", null, pw, new String[] { })

    }

}


interface OnNavigatedListener {

    fun onNavigated(destItem: NavItem)
}


data class AnimOptions(
    @AnimRes @AnimatorRes var enterAnim: Int = 0,
    @AnimRes @AnimatorRes var exitAnim: Int = 0,
    @AnimRes @AnimatorRes var popEnterAnim: Int = 0,
    @AnimRes @AnimatorRes var popExitAnim: Int = 0
) {
}


fun FragmentTransaction.setCustomAnimations(animOptions: AnimOptions): FragmentTransaction {
    return setCustomAnimations(
        animOptions.enterAnim,
        animOptions.exitAnim,
        animOptions.popEnterAnim,
        animOptions.popExitAnim
    )
}


