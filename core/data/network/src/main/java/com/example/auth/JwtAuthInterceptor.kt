package com.example.auth

import com.example.Urls
import com.example.data_store.AuthDataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class JwtAuthInterceptor @Inject constructor(
    private val authRepository: AuthDataStoreRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            authRepository.getAccessToken().first()
        }
        val request = chain.request().newBuilder()
        if (requiresAuth(chain.request())) {
            request.addHeader("Authorization", "Bearer $token")
        }
        val response = chain.proceed(request.build())
        if (response.code == 401) {
            runBlocking {
                authRepository.setUserRememberedFlag(false)
            }
            AuthEventBus.send(AppEvent.NavigateToLogin)
        }
        return response
    }

    private fun requiresAuth(request: Request): Boolean {
        val url = request.url.toString()
        return !url.contains(Urls.AUTH_API_URL + "/login")
    }
}