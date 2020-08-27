package tk.atna.wodthat.stuff

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener

object PlayServicesHelper {

    fun areAvailable(activity: AppCompatActivity): Boolean =
        GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(activity) == ConnectionResult.SUCCESS

    fun check(activity: AppCompatActivity, callback: ResultCallback) {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val code = googleApiAvailability.isGooglePlayServicesAvailable(activity)

        Log.w("-------------------- PLAY SERVICES CHECK CODE {$code}")

        when (code) {
            ConnectionResult.SUCCESS ->
                callback.onSuccess()
            //
            ConnectionResult.SERVICE_MISSING,
            ConnectionResult.SERVICE_INVALID ->
                callback.onFailure()
            //
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
            ConnectionResult.SERVICE_DISABLED,
            ConnectionResult.SERVICE_UPDATING ->
                makeAvailable(googleApiAvailability, activity, callback)

        }
    }

    private fun makeAvailable(
        googleApiAvailability: GoogleApiAvailability,
        activity: AppCompatActivity, callback: ResultCallback
    ) {
        googleApiAvailability.makeGooglePlayServicesAvailable(activity)
            .addOnCompleteListener(activity, OnCompleteListener {
                when {
                    // succeeded
                    it.isSuccessful -> callback.onSuccess()
                    // canceled
                    it.isCanceled -> callback.onFailure()
                    // failed
                    else -> {
                        it.exception?.printStackTrace()
                        callback.onFailure()
                    }
                }
            })
    }


    interface ResultCallback {

        fun onSuccess()

        fun onFailure(/*@NonNull Exception e*/)

    }

}
