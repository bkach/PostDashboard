package com.example.boris.postdashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boris.postdashboard.repository.Repository
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent
import kotlin.coroutines.CoroutineContext

class ViewModel constructor(
    private val intentInterpreter: Intent.IntentInterpreter,
    private val actionInterpreter: Action.ActionInterpreter,
    private val resultInterpreter: Result.ResultInterpreter,
    private val repository: Repository) : ViewModel(), KoinComponent, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State>
        get() = _state

    init {
        sendIntent(Intent.InitialIntent)
    }

    fun sendIntent(intent: Intent) {
        launch {
            intentInterpreter.interpret(intent) { action ->
                actionInterpreter.interpret(action) { result ->
                    resultInterpreter.interpret(result, ::changeState)
                }
            }
        }
    }

    private suspend fun changeState(nextState: State) {
        withContext(Dispatchers.Main) {
            _state.value = nextState
        }
    }

    override fun onCleared() {
        job.cancel()
        repository.job.cancel()
        super.onCleared()
    }

}
