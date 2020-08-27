package tk.atna.wodthat.stuff

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.lang.reflect.Type
import java.util.*

class PersistenceHelper private constructor(
    protected val preferences: SharedPreferences
) {

    constructor(context: Context, accountId: String? = null) : this(
        context.getSharedPreferences(
            accountId ?: context.packageName + "_preferences",
            Context.MODE_PRIVATE
        )
    )

    companion object {

        private val SP_LISTENERS =
            hashMapOf<String, SharedPreferences.OnSharedPreferenceChangeListener>()

        inline fun <reified T> readObject(context: Context, key: String): T? =
            PersistenceHelper(context).readObject<T>(key)

        inline fun <reified T> readObject(context: Context, key: String, type: Type): T? =
            PersistenceHelper(context).readObject<T>(key, type)

        inline fun <reified T> writeObject(context: Context, key: String, value: T?) =
            PersistenceHelper(context).writeObject<T>(key, value)

        fun readParameter(context: Context, key: String): String? =
            PersistenceHelper(context).readParameter(key)

        fun writeParameter(context: Context, key: String, value: String?) =
            PersistenceHelper(context).writeParameter(key, value)

        fun flushParameter(context: Context, key: String) =
            PersistenceHelper(context).writeParameter(key, null)

        fun getListeners(): HashMap<String, SharedPreferences.OnSharedPreferenceChangeListener> {
            // todo: check if map has leaks (dead listeners)
            return SP_LISTENERS
        }

    }

    // todo: listener CAN'T be a lambda, use anonymous class, for more info see
    // todo: http://mail.openjdk.java.net/pipermail/jdk8-dev/2014-March/004050.html
    inline fun <reified T> registerListener(key: String?, listener: OnChangeListener<T>?) {
        if (key != null
            && listener != null
        ) {
            val internalListener =
                SharedPreferences.OnSharedPreferenceChangeListener { sp, keyOfChanged ->
                    if (key == keyOfChanged) {
                        val json = sp.getString(key, null)
                        listener.onChanged(CompressHelper.fromJson<T>(json))
                    }
                }
            //
            preferences.registerOnSharedPreferenceChangeListener(internalListener)
            getListeners()[listener.toString()] = internalListener

        }
    }

    fun <T> unregisterListener(listener: OnChangeListener<T>?) =
        listener?.let {
            preferences.unregisterOnSharedPreferenceChangeListener(
                getListeners().remove(listener.toString())
            )
        }

    inline fun <reified T> readObject(key: String?): T? =
        key?.let {
            val json = readParameter(key)
//                Utils.logLongString(json)
            return CompressHelper.fromJson<T>(json)
        }

    inline fun <reified T> readObject(key: String?, type: Type): T? =
        key?.let {
            val json = readParameter(key)
            return CompressHelper.fromJson<T>(json, type)
        }

    inline fun <reified T> writeObject(key: String?, value: T?) =
        writeObject<T>(key, value, false)

    inline fun <reified T> writeObjectNow(key: String?, value: T?) =
        writeObject<T>(key, value, true)

    inline fun <reified T> writeObject(key: String?, value: T?, force: Boolean) =
        key?.let {
            // regular value
            if (value != null) {
                val json = CompressHelper.toJson<T>(value)
//                    Utils.logLongString(json)
                // serialized successfully
                json?.let {
                    writeParameter(key, json, force)
                }
                // no value - flush object
            } else
                flushParameter(key, force)
        }

    fun readParameter(key: String?): String? =
        key?.let {
            preferences.getString(key, null)
        }

    fun flushParameter(key: String?, force: Boolean) =
        key?.let {
            preferences.edit(commit = force) {
                remove(key)
            }
        }

    fun writeParameter(key: String?, value: String?) =
        writeParameter(key, value, false)

    fun writeParameter(key: String?, value: String?, force: Boolean) =
        key?.let {
            preferences.edit(commit = force) {
                putString(key, value)
            }
        }


    interface OnChangeListener<T> {

        fun onChanged(changed: T?)

//        fun type() =
//            (javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0]!!

    }

}
