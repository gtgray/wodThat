package tk.atna.wodthat.stuff

import tk.atna.wodthat.network.ApiClient

class SessionRepository(
    apiClient: ApiClient,
    persistenceHelper: PersistenceHelper
) : BaseRepository(apiClient) {

//    private val userAccount : UserAccount

    init {
//        userAccount = ProfileMapper.pullProfile(context, SessionManager.get().getActiveSessionOrThrow().accountId!!)!!
    }

//    suspend fun pullUserProfile(username: String): Resource<UserProfile> {
//        return serverApi.getUserProfile(username)
//            .map { source -> processResponse(source) }
//            .onErrorReturn { throwable -> processThrowable(throwable) }
//    }



}
