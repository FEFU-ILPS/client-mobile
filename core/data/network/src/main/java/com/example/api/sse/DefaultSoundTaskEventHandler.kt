package com.example.api.sse

import android.util.Log
import com.example.Urls
import com.example.api.dto.SoundSseEventDto
import com.google.gson.Gson
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import okhttp3.Headers
import java.net.URI
import java.time.Duration

class DefaultSoundTaskEventHandler private constructor(
    private val onSSEConnectionOpened: () -> Unit,
    private val onSSEConnectionClosed: () -> Unit,
    private val onSSEEventReceived: (event: String, messageEvent: MessageEvent) -> Unit,
    private val onSSEError: (t: Throwable) -> Unit
) : EventHandler {
    override fun onOpen() {
        onSSEConnectionOpened()
    }

    override fun onClosed() {
        onSSEConnectionClosed()
    }

    override fun onMessage(event: String, messageEvent: MessageEvent) {
        onSSEEventReceived(event, messageEvent)
    }

    override fun onError(t: Throwable) {
        onSSEError(t)
    }

    override fun onComment(comment: String) {
        Log.i("SSE_CONNECTION", comment)
    }

    companion object {
        fun startSseClient(
            soundTaskId: String,
            accessToken: String,
            onSSEConnectionOpened: () -> Unit,
            onSSEConnectionClosed: () -> Unit,
            onSSEEventReceived: (event: String, messageEvent: SoundSseEventDto) -> Unit,
            onSSEError: (t: Throwable) -> Unit
        ): EventSource {
            val handler = DefaultSoundTaskEventHandler(
                onSSEConnectionClosed = onSSEConnectionClosed,
                onSSEConnectionOpened = onSSEConnectionOpened,
                onSSEEventReceived = { event, messageEvent ->
                    val gson = Gson()
                    val soundEvent = gson.fromJson(messageEvent.data, SoundSseEventDto::class.java)
                    onSSEEventReceived(event, soundEvent)
                },
                onSSEError = onSSEError
            )
            val url = "${Urls.TASKS_API_URL}${soundTaskId}/stream"

            val headers = Headers.Builder()
                .add("Authorization", "Bearer $accessToken")
                .add("Accept", "text/event-stream")
                .build()
            val eventSource = EventSource.Builder(handler, URI.create(url))
                .headers(headers)
                .connectTimeout(Duration.ofMinutes(1))
                .build()

            eventSource.start()
            return eventSource
        }
    }
}