package com.example.auth

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AuthEventBus {
    private val _events = MutableSharedFlow<AppEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun send(event: AppEvent) {
        _events.tryEmit(event)
    }
}

sealed class AppEvent {
    object NavigateToLogin : AppEvent()
}