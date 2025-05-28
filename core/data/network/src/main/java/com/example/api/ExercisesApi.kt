package com.example.api

import com.example.api.dto.ExerciseListResponseDto
import com.example.api.dto.ExerciseResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExercisesApi {

    @GET(".")
    suspend fun getExercises(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ExerciseListResponseDto

    @GET("{uuid}/embedded")
    suspend fun getExercise(
        @Path("uuid") exerciseId: String,
        @Query("entities") embed: String = "text"
    ): ExerciseResponseDto
}