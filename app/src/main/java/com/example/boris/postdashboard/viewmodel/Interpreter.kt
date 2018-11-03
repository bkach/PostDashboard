package com.example.boris.postdashboard.viewmodel

abstract class Interpreter<INPUT, OUTPUT> {
    abstract suspend fun interpret(input: INPUT, callback: suspend (OUTPUT) -> Unit)
}