package tk.atna.wodthat.internal

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import tk.atna.wodthat.stuff.Log
import tk.atna.wodthat.stuff.LogoutHelper

open class BaseViewModel(context: Application) : AndroidViewModel(context) {

    @CallSuper
    override fun onCleared() {

        Log.w("------------- CLEARED ")
    }

    protected fun checkError(resultCode: ResultCode) {
        when (resultCode) {
            ResultCode.INVALID_SESSION -> LogoutHelper.logout(getApplication())
            else -> {}
        }
    }

}