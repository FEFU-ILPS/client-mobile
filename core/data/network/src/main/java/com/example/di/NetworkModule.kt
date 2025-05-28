package com.example.di

import com.example.Urls
import com.example.api.AuthApi
import com.example.api.FeedbackApi
import com.example.api.TasksApi
import com.example.api.ExercisesApi
import com.example.auth.JwtAuthInterceptor
import com.example.data_store.AuthDataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideAuthOkHttpClient(jwtAuthInterceptor: JwtAuthInterceptor): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .addInterceptor(jwtAuthInterceptor)
            .readTimeout(Duration.ofMinutes(1))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: AuthDataStoreRepository): JwtAuthInterceptor =
        JwtAuthInterceptor(tokenManager)

    @Provides
    @Singleton
    fun provideTextApi(okHttpClient: OkHttpClient): ExercisesApi {
        return Retrofit.Builder()
            .baseUrl(Urls.EXERCISES_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExercisesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFeedbackApi(okHttpClient: OkHttpClient): FeedbackApi {
        return Retrofit.Builder()
            .baseUrl(Urls.FEEDBACK_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FeedbackApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(okHttpClient: OkHttpClient): AuthApi {
        return Retrofit.Builder()
            .baseUrl(Urls.AUTH_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTasksApi(okHttpClient: OkHttpClient): TasksApi {
        return Retrofit.Builder()
            .baseUrl(Urls.TASKS_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TasksApi::class.java)
    }
}
