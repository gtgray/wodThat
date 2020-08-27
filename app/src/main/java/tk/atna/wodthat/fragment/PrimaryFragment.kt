package tk.atna.wodthat.fragment

import android.content.Context
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import tk.atna.wodthat.internal.FragmentAction
import tk.atna.wodthat.internal.FragmentActionCallback
import tk.atna.wodthat.stuff.Log

abstract class PrimaryFragment : ProgressableFragment(), FragmentActionCallback {

    protected var viewsPopulated = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        //
        arguments?.let {
            onProcessArguments(requireArguments())
        }

        // start listen back presses
        requireActivity().onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // if internal back press not consumed,
                    // fall back with common back press flow
                    val consumed = onBackPressed()

                    Log.w("---------------- BACK CONSUMED $consumed for ${this@PrimaryFragment}")

                    if (!consumed) {
                        isEnabled = false
                        //
//                            Bundle data = new Bundle()
//                            data.putBoolean(Params.BACK_CONSUMED, consumed)
                        makeFragmentAction(FragmentAction.BACK_PRESSED/*, data*/)
                    }
                }
            })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.w("------------------ CREATED $this")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //
        viewsPopulated = false
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.w("------------------ DESTROYED $this")
    }

//    override fun onStart() {
//        super.onStart()
//        //
//        if(needHideSoft())
//            Utils.hideSoftInput(getActivity())
//    }

//    fun needHideSoft() {
//        return true
//    }

    override fun showProgressInternal() {
        // override to use
    }

    override fun hideProgressInternal() {
        // override to use
    }

    protected open fun onProcessArguments(data: Bundle) {
        // override to use
        // todo: notice! children that could be further extended
        // todo: must override this method with annotation @CallSuper
    }

    // method to provide some new arguments to fragment.
    // can be called at any moment after fragment created.
    // args can contain, for example, detail id
    fun updateArguments(args: Bundle) {
        // override to use
    }

    @CallSuper
    override fun onAction(action: FragmentAction, data: Bundle?): Boolean {
        when (action) {
            FragmentAction.CLOSE -> {
                // go to first destination of current nav graph
                val navHostFragment = discoverNavHostFragment()
                navHostFragment?.let {
                    val navController = navHostFragment.navController
                    navController.popBackStack(navController.graph.startDestination, true)
                    //
                    val highNavController = getNavControllerUpward(navHostFragment)
                    highNavController?.popBackStack()
                    return true
                }
                //
                return true
            }
            else -> return false
        }
    }

    protected open fun onBackPressed(): Boolean {

//        Log.w("------------------ ON BACK PRESSED ")

        return false
    }

    protected fun onUpPressed() {

        Log.w("------------------ ON UP PRESSED ")

//        getNavController().popBackStack()
        requireActivity().onBackPressed()
    }

    protected fun getNavController(): NavController? = getNavControllerUpward(this)

    private fun discoverNavHostFragment(): NavHostFragment? = discoverNavHostFragmentUpward(this)

    protected fun getNavControllerUpward(startWith: Fragment): NavController? {
        val navHost = discoverNavHostFragmentUpward(startWith)
        return navHost?.navController
    }

    private fun discoverNavHostFragmentUpward(startWith: Fragment?): NavHostFragment? {
        if (startWith != null) {
            val parent = startWith.parentFragment
            if (parent is NavHostFragment)
                return parent
            // go higher
            return discoverNavHostFragmentUpward(parent)
        }
        //
        return null
    }

    protected fun makeFragmentAction(action: FragmentAction) = makeFragmentAction(action, null)

    protected fun makeFragmentAction(action: FragmentAction, data: Bundle?) {
        val consumed = doMakeFragmentAction(action, data, this)
        if (!consumed)
            throw UnsupportedOperationException("Action $action was not consumed")
    }

    private fun doMakeFragmentAction(
        action: FragmentAction,
        data: Bundle?,
        startWith: FragmentActionCallback?
    ): Boolean {
        startWith?.let {
            return when (startWith.onAction(action, data)) {
                true -> true
                false -> doMakeFragmentAction(action, data, getCallback(startWith))
            }
        }
        //
        return false
    }

    private fun getCallback(startWith: FragmentActionCallback): FragmentActionCallback? {
        val fragmentCallback = seekCallbackFragment(startWith as? Fragment)
        return fragmentCallback ?: activity as? FragmentActionCallback
    }

    private fun seekCallbackFragment(fragment: Fragment?): FragmentActionCallback? {
        fragment?.let {
            val parent = fragment.parentFragment
            if (parent is FragmentActionCallback)
                return parent
            // go higher
            return seekCallbackFragment(parent)
        }
        //
        return null
    }

}
