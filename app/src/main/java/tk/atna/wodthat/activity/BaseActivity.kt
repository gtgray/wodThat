package tk.atna.wodthat.activity

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import tk.atna.wodthat.internal.FragmentAction
import tk.atna.wodthat.internal.FragmentActionCallback
import tk.atna.wodthat.stuff.*

abstract class BaseActivity : AppCompatActivity(), FragmentActionCallback {

    override fun attachBaseContext(newBase: Context) =
        super.attachBaseContext(LocaleHelper.setupLocale(newBase))

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.w("--------------- CREATED $this")

        // lock orientation
        if (rotationLocked())
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        //
        super.onCreate(savedInstanceState)

        // provide actual status bar and system navigation bar heights
        InsetsHelper.discoverSystemInsets(window.decorView)
    }

    override fun onResume() {
        super.onResume()
//        // start listen network changes
//        NetworkStateReceiver.register(this)
    }

    override fun onPause() {
        super.onPause()
//        // stop listen network changes
//        NetworkStateReceiver.unregister(this)
    }

    @CallSuper
    override fun onAction(action: FragmentAction, data: Bundle?): Boolean {
        when (action) {
//            FragmentAction.LOGOUT -> {
//                data = new Bundle()
//                data.putBoolean(Params.LOGOUT, true)
//                display(this, LoginActivity.class,
////                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK,
//                        data)
//                finish()
//                return true
//            }
            FragmentAction.RESTART -> {
                restartApplication(this)
                return true
            }
            FragmentAction.RECREATE -> {
                recreate() // will try not restarting application but only activity
                return true
            }
            FragmentAction.BACK_PRESSED -> {

                Log.w("---------------- ACTIVITY BACK PRESSED")

                handleOnBackPressed()
                return true
            }
            FragmentAction.FINISH -> {
                super.onBackPressed()
                return true
            }
            //
            else -> return false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // process result to callback
        PermissionsHelper.getListener()
            ?.onRequestPermissionsResult(requestCode, arrayListOf(*permissions), grantResults)
    }

    protected open fun rotationLocked() = false

    @CallSuper
    protected open fun handleOnBackPressed() = super.onBackPressed()

}
