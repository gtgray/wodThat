package tk.atna.wodthat.stuff

import kotlinx.coroutines.*
import tk.atna.wodthat.internal.Resource
import tk.atna.wodthat.internal.ResultCode
import tk.atna.wodthat.model.*
import tk.atna.wodthat.network.ApiClient
import tk.atna.wodthat.network.model.*
import kotlin.coroutines.CoroutineContext

class WodsRepository(apiClient: ApiClient) : BaseRepository(apiClient) {


    suspend fun pullWods(): Resource<List<Wod>> {
//        return Resource(ResultCode.UNKNOWN_ERROR) // for testing purpose
        return coroutineScope {
            val wods = processSingleRequest {
                serverApi.getWods(0, 30/*500*/)
            }
            convertWods(source = wods)
        }
    }

    private suspend fun convertWods(
        context: CoroutineContext = Dispatchers.Default,
        source: Resource<WodsWrapper>
    ): Resource<List<Wod>> {
        return withContext(context) {
            val wods =
                if(source.resultCode == ResultCode.SUCCESS
                    && source.data != null)
                    source.data.results/*.reversed()*/
                else
                    listOf()
            //
            Resource(source.resultCode, wods, source.message)
        }
    }

    suspend fun pullWodDetails(wodCode: String): Resource<WodDetailed> {
//        return Resource(ResultCode.UNKNOWN_ERROR) // for testing purpose
        return supervisorScope {
//            withContext(Dispatchers.IO) {
                try {
                    // pull details
                    val details =
                        withContext(Dispatchers.IO) {
                            processResponse(serverApi.getWodDetails(wodCode))
                        }
                    // pull comments
                    val comments = async {
                        processResponse(serverApi.getWodComments(details.data!!.id))
                    }
                    // pull results
                    val results = async {
                        processResponse(serverApi.getWodResults(details.data!!.id, 0, details.data.resultsCount))
                    }
                    listOf(comments,  results).awaitAll()
                    //
                    mergeWodDetails(
                        details = details,
                        comments = comments.getCompleted(),
                        results = results.getCompleted()
                    )

                } catch (exception: Exception) {
                    processException<WodDetailed>(exception)
                }
//            }
        }
    }

    private suspend fun mergeWodDetails(
        context: CoroutineContext = Dispatchers.Default,
        details: Resource<WodDetailedModel>,
        comments: Resource<WodCommentsWrapper>,
        results: Resource<WodResultsWrapper>
    ): Resource<WodDetailed> {
        return withContext(context) {
            val wodDetailed = toWodDetailed(
                details.data!!,
                comments.data!!.results,
                results.data!!.results
            )
            Resource(details.resultCode, wodDetailed)
        }
    }

//    suspend fun pullWodResults(wodId: Int): Resource<List<WodResult>> {
//        return coroutineScope {
//            val results = processSingleRequest {
//                serverApi.getWodResults(wodId, 0, 500)
//            }
//            convertWodResults(source = results)
//        }
//    }

    private suspend fun convertWodResults(
        context: CoroutineContext = Dispatchers.Default,
        source: Resource<WodResultsWrapper>
    ): Resource<List<WodResultModel>> {
        return withContext(context) {
            val results =
                if(source.resultCode == ResultCode.SUCCESS
                    && source.data != null)
                    source.data.results/*.reversed()*/
                else
                    listOf()
            //
            Resource(source.resultCode, results, source.message)
        }
    }

}
