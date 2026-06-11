package com.buildstack.promptly.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.buildstack.promptly.domain.model.Chat
import com.buildstack.promptly.domain.repository.ChatRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val chatRepository: ChatRepository) : ViewModel() {
    val chats: StateFlow<List<Chat>> = chatRepository.getAllChats()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

class HomeViewModelFactory(
    private val chatRepository: ChatRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(chatRepository) as T
    }
}
