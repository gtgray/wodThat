package tk.atna.wodthat.stuff

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import tk.atna.wodthat.R
import tk.atna.wodthat.internal.DismissibleDialogWrapper

object PermissionsHelper {

    private val PERMISSION_REQUEST_CODE = PermissionsHelper::class.java.hashCode() and 0x0000FFFF
    private const val ALREADY_REQUESTED_PERMISSIONS = "permissions_requested"

    private var listener: OnRequestPermissionsResult? = null


    fun <T : FragmentActivity> checkAndRequestPermission(
        activity: T, callback: PermissionCallback,
        vararg permissions: Permission
    ) {
        checkAndRequestPermission(activity, true, callback, *permissions)
    }

    fun <T : FragmentActivity> checkAndRequestPermission(
        activity: T, rationaleFirst: Boolean,
        callback: PermissionCallback,
        vararg permissions: Permission
    ) {
        val deniedNames = arrayListOf<String>()
        var needGotoSettings = false

        // todo: read
        val alreadyRequested = pullAlreadyRequested(activity)

        // walk through permissions array
        permissions.forEach { permission ->
            val name = permission.name
            val checkResult = ContextCompat.checkSelfPermission(activity, name)

            Log.w("----------------- check permission $permission - $checkResult")

            // should be requested
            if (checkResult != PackageManager.PERMISSION_GRANTED) {
//                    denied.add(permission)
                deniedNames.add(name)
                // at least one permission is in hidden state
                // (user fired checkbox "don't ask again")
                val gotoSettings =
                    !ActivityCompat.shouldShowRequestPermissionRationale(activity, name)
                needGotoSettings = needGotoSettings and
                        gotoSettings and/*&&*/ alreadyRequested.contains(permission)
            }
            //
            alreadyRequested.add(permission)
        }

        // todo: write
        saveAlreadyRequested(activity, alreadyRequested)

        val deniedSize = deniedNames.size
        // success - all permissions granted
        if (deniedSize == 0) {
            callback.permissionsGranted()
            return
        }
        // need to request denied permissions
        // at first show rationale dialog
        val redefine = if (rationaleFirst) {
            shouldShowDialog(activity, deniedNames, false, needGotoSettings, callback)
        } else {
            requestPermissions(activity, deniedNames)
            true
        }
        // need to reinit callback
        if (redefine)
            redefineCallback(activity, deniedSize, rationaleFirst, callback, *permissions)
    }

    private fun pullAlreadyRequested(context: Context): MutableSet<Permission> {
        val alreadyRequested: MutableSet<Permission>? =
            PersistenceHelper.readObject(context, ALREADY_REQUESTED_PERMISSIONS)
        return alreadyRequested ?: mutableSetOf()
    }

    private fun saveAlreadyRequested(context: Context, permissions: Set<Permission>) {
        PersistenceHelper.writeObject(context, ALREADY_REQUESTED_PERMISSIONS, permissions)
    }

    private fun redefineCallback(
        activity: FragmentActivity, deniedSize: Int, cancelOnDenied: Boolean,
        callback: PermissionCallback, vararg permissions: Permission
    ) {
        listener = object : OnRequestPermissionsResult {
            override fun onRequestPermissionsResult(
                requestCode: Int,
                names: ArrayList<String>,
                grantResults: IntArray
            ) {
                if (requestCode == PERMISSION_REQUEST_CODE) {
                    when {
                        // previous permissions request isn't complete yet
                        (names.isEmpty() || grantResults.isEmpty()) -> callback.permissionsRequestFailed()
                        // not all permissions present in callback
                        (names.size != deniedSize || grantResults.size != deniedSize) ->
                            // make checking from start again
                            checkAndRequestPermission(
                                activity,
                                cancelOnDenied,
                                callback,
                                *permissions
                            )
                        // check results and provide
                        else -> checkGrantResults(
                            activity,
                            names,
                            grantResults,
                            cancelOnDenied,
                            callback
                        )
                    }
                }
            }
        }
    }

    fun getListener() = listener

    private fun checkGrantResults(
        activity: FragmentActivity, names: ArrayList<String>,
        grantResults: IntArray, cancelOnDenied: Boolean,
        callback: PermissionCallback
    ) {
        val denied = arrayListOf<String>()
        var needGotoSettings = false
        for (i in 0..names.size) {
            val name = names[i]

            Log.w("----------------- check grant results $name, ${grantResults[i]}")

            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                denied.add(name)
                // at least one permission is in hidden state
                // (user fired checkbox "don't ask again")
                needGotoSettings = needGotoSettings and
                        !ActivityCompat.shouldShowRequestPermissionRationale(activity, name)
            }
        }
        //
        shouldShowDialog(activity, denied, cancelOnDenied, needGotoSettings, callback)

    }

    private fun shouldShowDialog(
        activity: FragmentActivity, deniedNames: ArrayList<String>,
        cancelOnDenied: Boolean, needGotoSettings: Boolean,
        callback: PermissionCallback
    ): Boolean {
        // number of permissions weren't granted
        if (deniedNames.isNotEmpty()) {
            // just cancel
            if (cancelOnDenied) {
                callback.permissionsRequestCanceled()
                return false
            }
            // should go to settings to grant permissions
            if (needGotoSettings)
                callback.showGotoSettingsDialog(activity)
            else
            // should show rationale
                callback.showRationaleDialog(activity, deniedNames)
            //
            return true
        }
        // success - all permissions granted
        callback.permissionsGranted()
        return false
    }

    private fun requestPermissions(activity: FragmentActivity, permissionNames: ArrayList<String>) {

        Log.w("----------------- requestPermissions $permissionNames")

        ActivityCompat.requestPermissions(
            activity,
            permissionNames.toTypedArray(),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun gotoAppSettings(activity: FragmentActivity) {
        val uri = Uri.fromParts("package", activity.packageName, null)
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.data = uri
        activity.startActivityForResult(intent, PERMISSION_REQUEST_CODE)
    }


    interface OnRequestPermissionsResult {

        fun onRequestPermissionsResult(
            requestCode: Int,
            names: ArrayList<String>,
            grantResults: IntArray
        )

    }


    abstract class PermissionCallback : Callback {

        override fun showRationaleDialog(activity: FragmentActivity, names: ArrayList<String>) {
            activity?.let {
                DismissibleDialogWrapper.aware(activity,
                    AlertDialog.Builder(activity/*, R.style.Style_Dialog_DarkPrimary*/)
                        .setMessage(Permission.RATIONALE)
                        .setPositiveButton(R.string.ok) { dialog, which ->
                            requestPermissions(activity, names)
                        }
                        .setNegativeButton(R.string.cancel) { dialog, which ->
                            permissionsRequestCanceled()
                        }
                        .setCancelable(false)
                        .show())
            }
        }

        override fun showGotoSettingsDialog(activity: FragmentActivity) {
            DismissibleDialogWrapper.aware(activity,
                AlertDialog.Builder(activity/*, R.style.Style_Dialog_DarkPrimary*/)
                    .setMessage(Permission.RATIONALE)
                    .setPositiveButton(R.string.settings) { dialog, which ->
                        gotoAppSettings(activity)
                    }
                    .setNegativeButton(R.string.cancel) { dialog, which ->
                        permissionsRequestCanceled()
                    }
                    .setCancelable(false)
                    .show())
        }
    }


    interface Callback {

        fun showRationaleDialog(activity: FragmentActivity, names: ArrayList<String>)

        fun showGotoSettingsDialog(activity: FragmentActivity)

        fun permissionsGranted()

        fun permissionsRequestCanceled()

        fun permissionsRequestFailed() {
            // override to use
        }

    }


    enum class Permission(val permissionName: String) {
//        String title : String
//        String rationale : String
//        @DrawableRes icon : Int

        //        ACCESS_COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION),
        READ_PHONE_STATE(Manifest.permission.READ_PHONE_STATE),

        //        RECEIVE_SMS(Manifest.permission.RECEIVE_SMS, "Requested permissions are required for application to work properly"),
        READ_SMS(Manifest.permission.READ_SMS),

        //        SEND_SMS(Manifest.permission.SEND_SMS),
        READ_CALL_LOG(Manifest.permission.READ_CALL_LOG),
        ;

        companion object {
            //            const val TITLE = "Permissions request"
            const val RATIONALE =
                "To activate the service, please allow the application to check mobile network settings of your device."
//            const val @DrawableRes ICON = android.R.drawable.stat_sys_warning
        }

    }

}
