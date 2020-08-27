package tk.atna.wodthat.stuff

import tk.atna.wodthat.BuildConfig

object Log {

    init {
        Timber.plant(Timber.DebugTree(Log::class.java.name))
    }

    fun v(message: String) {
        if (BuildConfig.DEBUG)
            Timber.v(message)
    }

    fun d(message: String) {
        if (BuildConfig.DEBUG)
            Timber.d(message)
    }

    fun i(message: String) {
        if (BuildConfig.DEBUG)
            Timber.i(message)
    }

    fun w(message: String) {
        if (BuildConfig.DEBUG)
            Timber.w(message)
    }

    fun e(message: String) {
        if (BuildConfig.DEBUG)
            Timber.e(message)
    }

}