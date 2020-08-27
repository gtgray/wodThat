package tk.atna.wodthat.internal

import android.app.Application
import tk.atna.wodthat.network.ApiClient
import tk.atna.wodthat.stuff.PersistenceHelper
import tk.atna.wodthat.stuff.SessionRepository

class SessionViewModel(context: Application) : BaseViewModel(context) {

    private val repository: SessionRepository
//    private val liveExercises = MutableLiveData<Resource<List<Exercise>>>()

    init {
        repository = SessionRepository(
            ApiClient.getInstance(context),
            PersistenceHelper(context)
        )
    }

    fun flushCache() {
//        liveExercises.value = null
    }



}