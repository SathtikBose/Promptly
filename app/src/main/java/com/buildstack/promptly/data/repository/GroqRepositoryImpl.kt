package com.buildstack.promptly.data.repository

import com.buildstack.promptly.BuildConfig
import com.buildstack.promptly.data.remote.GroqApi
import com.buildstack.promptly.data.remote.dto.GroqMessage
import com.buildstack.promptly.data.remote.dto.GroqRequest
import com.buildstack.promptly.domain.repository.GroqRepository

class GroqRepositoryImpl(
    private val api: GroqApi
) : GroqRepository {
    override suspend fun generateResponse(messages: List<Pair<String, String>>): Result<String> {
        return try {
            val groqMessages = messages.map { GroqMessage(role = it.first, content = it.second) }
            val request = GroqRequest(messages = groqMessages)
            
            val response = api.getChatCompletion(
                authHeader = "Bearer ${BuildConfig.GROQ_API_KEY}",
                request = request
            )
            
            val content = response.choices.firstOrNull()?.message?.content
            if (content != null) {
                Result.success(content)
            } else {
                Result.failure(Exception("Empty response from Groq API"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
