package tk.atna.wodthat.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import tk.atna.wodthat.R
import tk.atna.wodthat.databinding.ActivityMainBinding
import tk.atna.wodthat.internal.*
import tk.atna.wodthat.stuff.PlayServicesHelper
import tk.atna.wodthat.stuff.dimenToPixels

class MainActivity : BaseActivity(), BottomNavigation {

    private val LAYOUT = R.layout.activity_main

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: MainNavController

    private var bottomNavSize: Int = 0
    private var bottomNavTranslationY: Int = 0
    private var containerBottomPadding: Int = 0

    private val logoutReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
//            onAction(FragmentAction.LOGOUT, null)
//            viewModel.signout()
            onAction(FragmentAction.RESTART, null)
        }
    }

    companion object {
        private const val NAV_CONTROLLER_STATE = "nav_controller_state"
        private const val NAV_TRANSLATION_Y = "nav_translation_y"
        private const val CONTAINER_BOTTOM_PADDING = "container_bottom_padding"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // views
        binding = DataBindingUtil.setContentView(this, LAYOUT)
        // restore state
        var navControllerState: Bundle? = null
        when (savedInstanceState != null) {
            true -> with(savedInstanceState) {
                navControllerState = getBundle(NAV_CONTROLLER_STATE)
                bottomNavTranslationY = getInt(NAV_TRANSLATION_Y)
                containerBottomPadding = getInt(CONTAINER_BOTTOM_PADDING)
                binding.vgMainContainer.setPadding(0, 0, 0, containerBottomPadding)
            }
            // first launch
            false -> {
                bottomNavTranslationY = 0
                containerBottomPadding = binding.vgMainContainer.paddingBottom
                //
//                viewModel.onNewIntent(intent)
            }
        }

        bottomNavSize = dimenToPixels(this, BottomNavigation.DEFAULT_NAV_SIZE)
        //
        with(binding.vgBottomNavigation) {
            // start listen items selection
            setOnNavigationItemSelectedListener { item ->
                navController.navigate(NavItem.getById(item.itemId))
                true
            }
            // start listen items reselection
            setOnNavigationItemReselectedListener { item ->
                navController.navigateReselected(NavItem.getById(item.itemId))
            }
            //
            translationY = bottomNavTranslationY.toFloat()
        }

        //
        navController = MainNavController(supportFragmentManager, R.id.vg_main_container)
        with(navController) {
            restoreState(navControllerState)
            startNavigation()
            setNavigatedListener(object : OnNavigatedListener {
                override fun onNavigated(destItem: NavItem) {
                    binding.vgBottomNavigation.menu.findItem(destItem.itemId).isChecked = true
                }
            })
        }

        // check play services availability
        PlayServicesHelper.check(this, object : PlayServicesHelper.ResultCallback {
            override fun onSuccess() {

//                Log.w("-------------------- PLAY CHECK SUCCESS ")

//                FcmRepository(this@MainActivity).discoverFcmToken()
            }

            override fun onFailure() {
                DismissibleDialogWrapper.aware(
                    this@MainActivity,
                    AlertDialog.Builder(this@MainActivity/*, R.style.Style_Dialog_DarkPrimary*/)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.play_services_error)
                        .setPositiveButton(R.string.ok, null)
                        .setCancelable(false)
                        .show()
                )
            }
        })

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //
        outState.putBundle(NAV_CONTROLLER_STATE, navController.saveState())
        outState.putInt(NAV_TRANSLATION_Y, bottomNavTranslationY)
        outState.putInt(CONTAINER_BOTTOM_PADDING, containerBottomPadding)
    }

    override fun onAction(action: FragmentAction, data: Bundle?): Boolean {
        when (action) {
//            GO_UP -> {
//                onBackPressed()
//                return true
//            }
//            FragmentAction.GOTO_XXX -> {
//                navController.navigate(NavItem.XXX)
//                return true
//            }
            else -> return super.onAction(action, data)
        }
    }

    override fun rotationLocked() = true

    override fun handleOnBackPressed() {

//        Log.w("--------------- BACK PRESSED ")

        if (!navController.onBackHandled())
            super.handleOnBackPressed()
    }

    override fun showBottomNavigation(show: Boolean) {
        // flush padding on nav hiding
        if (!show)
            binding.vgMainContainer.setPadding(0, 0, 0, 0)
        //
        val translation = if (show) 0 else bottomNavSize

//        Log.i("--------------- SHOW " + bottomNavTranslationY + ", " + translation)

        if (bottomNavTranslationY != translation) {
            bottomNavTranslationY = translation
            containerBottomPadding = bottomNavSize - translation
            //
            val translationY = ObjectAnimator.ofFloat(
                binding.vgBottomNavigation, View.TRANSLATION_Y, bottomNavTranslationY.toFloat()
            )
            //
            val padding = ValueAnimator.ofInt(containerBottomPadding)
            if (show) {
                padding.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int

//                    Log.w("----------- VALUE " + value)

                    binding.vgMainContainer.setPaddingRelative(0, 0, 0, value)
                }
            }
            // animate bottom navigation view appearing/disappearing
            with(AnimatorSet()) {
//                setStartDelay(200)
//                setDuration(200)
                duration = 10
                playTogether(translationY, padding)
                start()
            }

        }

    }


}
