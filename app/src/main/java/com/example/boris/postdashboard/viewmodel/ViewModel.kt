package com.example.boris.postdashboard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModel constructor(
    private val intentInterpreter: Intent.IntentInterpreter,
    private val actionInterpreter: Action.ActionInterpreter,
    private val resultInterpreter: Result.ResultInterpreter) : ViewModel() {

    val state: MutableLiveData<State> = MutableLiveData()

    override fun onCleared() {
        // TODO: cancel jobs!
//        job.cancel()
        super.onCleared()
    }

    init {
       setIntent(Intent.InitialIntent)
    }

    // TODO: The whole flow should be encapsulated in a LiveData!
    fun setIntent(intent: Intent) {
        GlobalScope.launch {
            val action: Action = intentInterpreter.interpret(intent)
            val result: Result = actionInterpreter.interpret(action).await()
            withContext(Dispatchers.Main) {
                state.value = resultInterpreter.interpret(result)
                System.out.println("bdebug $intent")
                System.out.println("bdebug $action")
                System.out.println("bdebug $result")
                System.out.println("bdebug ${state.value}")
            }
        }
    }
}