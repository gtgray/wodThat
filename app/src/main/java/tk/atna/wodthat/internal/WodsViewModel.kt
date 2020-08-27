package tk.atna.wodthat.internal

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import tk.atna.wodthat.model.Wod
import tk.atna.wodthat.model.WodDetailed
import tk.atna.wodthat.model.WodResult
import tk.atna.wodthat.network.ApiClient
import tk.atna.wodthat.stuff.WodsRepository

class WodsViewModel(context: Application) : BaseViewModel(context) {

    private val repository: WodsRepository
    private val liveWods = MutableLiveData<Resource<List<Wod>>>()
    private val liveWodDetailsMap = hashMapOf<String, MutableLiveData<Resource<WodDetailed>>>()
//    private val liveWodResultsMap = hashMapOf<Int, MutableLiveData<Resource<List<WodResult>>>>()

    init {
        repository = WodsRepository(ApiClient.getInstance(context))
    }

    fun flushCache() {
        liveWods.value = null
    }

    fun getWods(): LiveData<Resource<List<Wod>>> {
        // if NO value - load from server
        liveWods.value ?: pullWods()
        //
        return liveWods
    }

    private fun pullWods() {
        viewModelScope.launch {
            liveWods.value = Resource(ResultCode.IN_PROGRESS)
            liveWods.postValue(
                repository.pullWods()
                    .also { resource -> checkError(resource.resultCode) }
            )
        }
    }

    fun getWodDetails(wodCode: String): LiveData<Resource<WodDetailed>> {
        var details = liveWodDetailsMap[wodCode]
        when {
            // first launch
            (details == null) -> {
                details = MutableLiveData()
                liveWodDetailsMap[wodCode] = details
                // load from server
                pullWodDetails(wodCode)
            }
            // failed before
            (details.value?.resultCode != ResultCode.SUCCESS) -> {
                // load from server
                pullWodDetails(wodCode)
            }
        }
        //
        return details!!
    }

    private fun pullWodDetails(wodCode: String) {
        viewModelScope.launch {
            val details = liveWodDetailsMap[wodCode]
            details ?: throw IllegalStateException("Cached details for '$wodCode' can't be null")
            //
            details.value = Resource(ResultCode.IN_PROGRESS)
            details.postValue(
                repository.pullWodDetails(wodCode)
                    .also { resource -> checkError(resource.resultCode) }
            )
        }
    }

    fun getWodResults(wodCode: String): LiveData<List<WodResult>> {
        val details = liveWodDetailsMap[wodCode]
        details ?: throw IllegalStateException("Cached details for '$wodCode' can't be null")
        //
        return liveData {
            emit(details.value!!.data!!.results)
        }
    }
//    fun getWodResults(wodId: Int): LiveData<Resource<List<WodResult>>> {
//        var results = liveWodResultsMap[wodId]
//        if (results == null) {
//            results = MutableLiveData()
//            liveWodResultsMap[wodId] = results
//            // load from server
//            pullWodResults(wodId)
//        }
//        //
//        return results
//    }

//    private fun pullWodResults(wodId: Int) {
//        viewModelScope.launch {
//            val results = liveWodResultsMap[wodId]
//            results ?: throw IllegalStateException("Cached details for '$wodId' can't be null")
//            //
//            results.value = Resource(ResultCode.IN_PROGRESS)
//            results.postValue(
//                repository.pullWodResults(wodId)
//                    .also { resource -> checkError(resource.resultCode) }
//            )
//        }
//    }

}