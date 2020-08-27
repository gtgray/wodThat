package tk.atna.wodthat.stuff

import android.content.Context

class SessionManager private constructor(
    private val persistenceHelper: PersistenceHelper
) {

    private var session: Session? = null

    companion object {
        @Volatile
        private var INSTANCE: SessionManager? = null

        private const val SESSION = "session"

        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context)
                    .also { INSTANCE = it }
            }
        }
    }

    private constructor(context: Context) : this(PersistenceHelper(context)) {
        // pull previous session
        restoreSession()
    }

    fun getSession() = session

    fun hasSession() = session != null

    fun activateSession(session: Session): Session {
        this.session = session
        saveSession()
        return session
    }

    fun flushSession() {
        session = null
        saveSession()

        Log.w("------------- FLUSH SESSION ")

    }

    private fun restoreSession() {
        session = persistenceHelper.readObject(SESSION, Session::class.java)
    }

    private fun saveSession() = persistenceHelper.writeObjectNow(SESSION, session)


    data class Session(
        val accountId: Long//? = null
    ) {

//        val isActive
//            get() = accountId != null
    }

}