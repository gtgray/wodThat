package tk.atna.wodthat.stuff

import android.app.Application
import android.content.Context
import android.content.res.Configuration

class WodThatApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.setupLocale(base))
    }

    override fun onCreate() {
        super.onCreate()

        // init some stuff
    }

    override fun onConfigurationChanged(newConfig : Configuration) {
        super.onConfigurationChanged(newConfig)

//        Log.w("----------------- CHANGED " + newConfig.locale)

        // todo: implement!
        // todo: prevent locale changes when user changed language in settings
    }


}