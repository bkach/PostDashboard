package com.example.boris.postdashboard.viewmodel

abstract class Interpreter<INPUT, OUTPUT> {
    abstract fun interpret(input: INPUT) : OUTPUT
}