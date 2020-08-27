package tk.atna.wodthat.stuff

import kotlinx.coroutines.*
import retrofit2.Response
import tk.atna.wodthat.internal.Resource
import tk.atna.wodthat.internal.ResultCode
import tk.atna.wodthat.internal.ResultException
import tk.atna.wodthat.network.ApiClient
import tk.atna.wodthat.network.ServerApi
import java.io.IOException
import kotlin.coroutines.CoroutineContext

open class BaseRepository(apiClient: ApiClient) {

    protected val serverApi: ServerApi

    init {
        serverApi = apiClient
            .enableLogging()
            .createService(ServerApi::class.java)
    }

    protected suspend fun <T> processSingleRequest(
        context: CoroutineContext = Dispatchers.IO,
        block: suspend () -> Response<T>
    ): Resource<T> {
        return withContext(context) {
            try {
                val response = block()
                processResponse(response)

            } catch (exception: Exception) {
                processException<T>(exception)
            }
        }
    }

    @Throws(ResultException::class)
    protected fun <T> processResponse(source: Response<T>): Resource<T> {

        Log.w("----------------- CODE " + source.code())

        return when (source.code()) {
            200 -> Resource(ResultCode.SUCCESS, source.body())
// todo: discover possible error codes
//            204 -> Resource(ResultCode.SUCCESS_EMPTY)
//            400 -> throw ResultException(ResultCode.INVALID_ARGUMENTS)
//            401/*, 403*/ -> throw ResultException(ResultCode.INVALID_SESSION)
            else -> throw ResultException(ResultCode.UNKNOWN_ERROR)
        }
    }

    protected fun <T> processException(exception: Exception): Resource<T> {
        exception.printStackTrace()
        return when (exception) {
            is ResultException -> Resource<T>(exception.resultCode)
            else -> Resource(ResultCode.UNKNOWN_ERROR, message = exception.message)
        }
    }


}
