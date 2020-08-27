package tk.atna.wodthat.internal

import android.os.Bundle

/**
 * Callback interface to deliver fragment actions to hoster (activity/fragment)
 */
interface FragmentActionCallback {

    /**
     * Called on fragment action event
     *
     * @param action needed command
     * @param data   additional data to send
     * @return true if action was consumed, false otherwise
     */
    fun onAction(action: FragmentAction, data: Bundle?): Boolean

}