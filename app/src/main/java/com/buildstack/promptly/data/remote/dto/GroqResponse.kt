package com.buildstack.promptly.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroqResponse(
    @SerialName("choices") val choices: List<GroqChoice>
)

@Serializable
data class GroqChoice(
    @SerialName("message") val message: GroqResponseMessage
)

@Serializable
data class GroqResponseMessage(
    @SerialName("content") val content: String
)
