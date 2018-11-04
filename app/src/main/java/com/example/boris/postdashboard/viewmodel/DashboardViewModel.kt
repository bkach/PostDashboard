package com.example.boris.postdashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import kotlin.coroutines.CoroutineContext

class DashboardViewModel constructor(
    private val intentInterpreter: Intent.IntentInterpreter,
    private val actionInterpreter: Action.ActionInterpreter,
    private val resultInterpreter: Result.ResultInterpreter,
    private val contextPool: CoroutineContextProvider) : ViewModel(), KoinComponent, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = contextPool.IO

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State>
        get() = _state

    fun sendIntent(intent: Intent) {
        launch(contextPool.IO) {
            intentInterpreter.interpret(intent) { action ->
                actionInterpreter.interpret(action) { result ->
                    resultInterpreter.interpret(result, ::changeState)
                }
            }
        }
    }

    private suspend fun changeState(nextState: State) {
        launch(contextPool.Main) {
            _state.value = nextState
        }
    }

    public override fun onCleared() {
        contextPool.cancelJob()
        super.onCleared()
    }

}
