package tk.atna.wodthat.stuff

import android.content.Context
import java.util.*

object LocaleHelper {

    private const val APP_LOCALE = "app_locale"


//    public static Locale getCurrentLocale(@NonNull Context context) {
//        return context.getResources().getConfiguration().locale
//    }

    fun setupLocale(context: Context): Context {
        val saved = pullAppLocale(context)
        return if (saved != null)
            updateConfiguration(context, saved)
        else
            context
    }

    private fun updateConfiguration(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        //
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    fun updateAppLocale(context: Context, locale: Locale?) =
        saveAppLocale(context, locale)

    private fun pullAppLocale(context: Context) =
        PersistenceHelper.readObject<Locale>(context, APP_LOCALE)

    private fun saveAppLocale(context: Context, locale: Locale?) =
        PersistenceHelper.writeObject(context, APP_LOCALE, locale)

}
