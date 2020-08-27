package tk.atna.wodthat.stuff

import kotlinx.coroutines.*
import tk.atna.wodthat.internal.Resource
import tk.atna.wodthat.internal.ResultCode
import tk.atna.wodthat.model.Exercise
import tk.atna.wodthat.model.ExerciseDetailed
import tk.atna.wodthat.network.model.ExercisesWrapper
import tk.atna.wodthat.network.ApiClient
import kotlin.coroutines.CoroutineContext

class ExercisesRepository(apiClient: ApiClient) : BaseRepository(apiClient) {

//    private val userAccount : UserAccount

    init {
//        userAccount = ProfileMapper.pullProfile(context, SessionManager.get().getActiveSessionOrThrow().accountId!!)!!
    }

//    suspend fun pullUserProfile(username: String): Resource<UserProfile> {
//        return serverApi.getUserProfile(username)
//            .map { source -> processResponse(source) }
//            .onErrorReturn { throwable -> processThrowable(throwable) }
//    }

    suspend fun pullExercises(): Resource<List<Exercise>> {
//        return Resource(ResultCode.UNKNOWN_ERROR) // for testing purpose
        return coroutineScope {
            val exercises = processSingleRequest {
                serverApi.getExercises(0, 50/*500*/, "popular")
            }
            //
            convert(source = exercises)
        }
    }

    private suspend fun convert(
        context: CoroutineContext = Dispatchers.Default,
        source: Resource<ExercisesWrapper>
    ): Resource<List<Exercise>> {
        return withContext(context) {
            val exercises =
                if(source.resultCode == ResultCode.SUCCESS
                    && source.data != null)
                    source.data.results.reversed()
                else
                    listOf()
            //
            Resource(source.resultCode, exercises, source.message)
        }
    }

    suspend fun pullExerciseDetails(exerciseCode: String): Resource<ExerciseDetailed> {
//        return Resource(ResultCode.UNKNOWN_ERROR) // for testing purpose
        return processSingleRequest {
            serverApi.getExerciseDetails(exerciseCode)
        }
    }

}
