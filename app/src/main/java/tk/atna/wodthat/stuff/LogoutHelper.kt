package tk.atna.wodthat.stuff

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

object LogoutHelper {

    private const val ACTION_LOGOUT = "action.logout"
    private val FILTER_LOGOUT = IntentFilter(ACTION_LOGOUT)

    fun registerReceiver(context: Context?, receiver: BroadcastReceiver?) {
        receiver ?: return
        context?.registerReceiver(receiver, FILTER_LOGOUT)
    }

    fun unregisterReceiver(context: Context?, receiver: BroadcastReceiver?) {
        receiver ?: return
        context?.unregisterReceiver(receiver)
    }

    fun logout(context: Context?) = context?.sendBroadcast(Intent(ACTION_LOGOUT))

}
