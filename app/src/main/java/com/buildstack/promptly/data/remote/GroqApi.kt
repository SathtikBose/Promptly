package com.buildstack.promptly.data.remote

import com.buildstack.promptly.data.remote.dto.GroqRequest
import com.buildstack.promptly.data.remote.dto.GroqResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GroqApi {
    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") authHeader: String,
        @Body request: GroqRequest
    ): GroqResponse

    companion object {
        const val BASE_URL = "https://api.groq.com/openai/v1/"
    }
}
