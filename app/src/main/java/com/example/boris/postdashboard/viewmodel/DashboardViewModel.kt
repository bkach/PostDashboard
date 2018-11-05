/*
 * PostDashboard
 * Copyright (C) 2018 Boris Kachscovsky
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.example.boris.postdashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel for the PostDashboard Application
 */
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

    /**
     * This function pieces together each part of the View Models' architecture. Each [Intent] becomes a [State], and
     * in the process takes an [Action].
     *
     * The basic idea:
     *      Intents -> Actions -> (Action taken) -> Result -> State
     */
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
