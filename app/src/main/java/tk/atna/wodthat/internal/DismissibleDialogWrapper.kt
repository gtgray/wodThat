package tk.atna.wodthat.internal

import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class DismissibleDialogWrapper(
    owner: LifecycleOwner,
    dialog: AlertDialog
) {

    init {
        // start listen to owner destroying event to prevent dialog window to be leaked
        owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                dialog.dismiss()
            }
        })
    }

    companion object {
        fun aware(owner: LifecycleOwner, dialog: AlertDialog) =
            DismissibleDialogWrapper(owner, dialog)
    }

}
