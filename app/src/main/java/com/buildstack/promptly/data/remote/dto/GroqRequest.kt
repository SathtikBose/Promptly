package com.buildstack.promptly.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroqRequest(
    @SerialName("model") val model: String = "llama-3.3-70b-versatile",
    @SerialName("messages") val messages: List<GroqMessage>
)

@Serializable
data class GroqMessage(
    @SerialName("role") val role: String,
    @SerialName("content") val content: String
)
