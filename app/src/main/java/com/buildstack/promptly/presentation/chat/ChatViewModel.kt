package com.buildstack.promptly.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.buildstack.promptly.domain.model.Chat
import com.buildstack.promptly.domain.model.Message
import com.buildstack.promptly.domain.repository.ChatRepository
import com.buildstack.promptly.domain.repository.GroqRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val groqRepository: GroqRepository,
    private val chatId: Long // 0 means new chat
) : ViewModel() {

    private val _currentChatId = MutableStateFlow(chatId)

    val messages: StateFlow<List<Message>> = _currentChatId.flatMapLatest { id ->
        if (id == 0L) flowOf(emptyList())
        else chatRepository.getMessagesForChat(id)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating = _isGenerating.asStateFlow()

    fun sendMessage(content: String) {
        if (content.isBlank() || _isGenerating.value) return

        viewModelScope.launch {
            _isGenerating.value = true
            
            // 1. Create chat if it doesn't exist
            val activeChatId = if (_currentChatId.value == 0L) {
                val newId = chatRepository.createChat("New Chat")
                _currentChatId.value = newId
                newId
            } else {
                _currentChatId.value
            }

            // 2. Save user message
            chatRepository.addMessage(activeChatId, "user", content)

            // 3. Prepare message history for API
            val history = messages.value.map { Pair(it.role, it.content) } + Pair("user", content)

            // 4. Call Groq API
            val result = groqRepository.generateResponse(history)

            result.onSuccess { aiResponse ->
                // 5. Save AI response
                chatRepository.addMessage(activeChatId, "assistant", aiResponse)

                // 6. Generate Title if it's the first message
                if (history.size == 1) {
                    generateTitle(activeChatId, content, aiResponse)
                }
            }.onFailure {
                // Add error message
                chatRepository.addMessage(activeChatId, "assistant", "Error: ${it.message}")
            }

            _isGenerating.value = false
        }
    }

    private fun generateTitle(chatId: Long, userMessage: String, aiResponse: String) {
        viewModelScope.launch {
            val prompt = listOf(
                Pair("user", "Conversation Context: User said: '$userMessage', AI replied: '$aiResponse'. Generate a short title under 5 words for this conversation.")
            )
            groqRepository.generateResponse(prompt).onSuccess { title ->
                // Clean up title quotes if any
                val cleanTitle = title.removePrefix("\"").removeSuffix("\"").trim()
                chatRepository.updateChatTitle(chatId, cleanTitle)
            }
        }
    }

    fun deleteChat() {
        if (_currentChatId.value == 0L) return
        viewModelScope.launch {
            chatRepository.getChatById(_currentChatId.value)?.let {
                chatRepository.deleteChat(it)
                _currentChatId.value = 0L
            }
        }
    }
}

class ChatViewModelFactory(
    private val chatRepository: ChatRepository,
    private val groqRepository: GroqRepository,
    private val chatId: Long
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(chatRepository, groqRepository, chatId) as T
    }
}
