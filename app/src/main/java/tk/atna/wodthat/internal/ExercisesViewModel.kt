package tk.atna.wodthat.internal

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import tk.atna.wodthat.model.Exercise
import tk.atna.wodthat.model.ExerciseDetailed
import tk.atna.wodthat.network.ApiClient
import tk.atna.wodthat.stuff.ExercisesRepository

class ExercisesViewModel(context: Application) : BaseViewModel(context) {

    private val repository: ExercisesRepository
    private val liveExercises = MutableLiveData<Resource<List<Exercise>>>()
    private val liveExerciseDetailsMap = hashMapOf<String, MutableLiveData<Resource<ExerciseDetailed>>>()

    init {
        repository = ExercisesRepository(ApiClient.getInstance(context))
    }

    fun flushCache() {
        liveExercises.value = null
    }

    fun getExercises(): LiveData<Resource<List<Exercise>>> {
        // if NO value - load from server
        liveExercises.value ?: pullExercises()
        //
        return liveExercises
    }

    private fun pullExercises() {
        viewModelScope.launch {
            liveExercises.value = Resource(ResultCode.IN_PROGRESS)
            liveExercises.postValue(
                repository.pullExercises()
                    .also { resource -> checkError(resource.resultCode) }
            )
        }
    }

    fun getExerciseDetails(exerciseCode: String): LiveData<Resource<ExerciseDetailed>> {
        var details = liveExerciseDetailsMap[exerciseCode]
        if (details == null) {
            details = MutableLiveData()
            liveExerciseDetailsMap[exerciseCode] = details
            // load from server
            pullExerciseDetails(exerciseCode)
        }
        //
        return details
    }

    private fun pullExerciseDetails(exerciseCode: String) {
        viewModelScope.launch {
            val details = liveExerciseDetailsMap[exerciseCode]
            details ?: throw IllegalStateException("Cached details for '$exerciseCode' can't be null")
            //
            details.value = Resource(ResultCode.IN_PROGRESS)
            details.postValue(
                repository.pullExerciseDetails(exerciseCode)
                    .also { resource -> checkError(resource.resultCode) }
            )
        }
    }

}