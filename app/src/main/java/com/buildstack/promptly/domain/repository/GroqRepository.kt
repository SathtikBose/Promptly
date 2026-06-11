package com.buildstack.promptly.domain.repository

interface GroqRepository {
    suspend fun generateResponse(messages: List<Pair<String, String>>): Result<String>
}
