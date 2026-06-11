package com.buildstack.promptly.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.buildstack.promptly.di.AppContainer
import com.buildstack.promptly.presentation.chat.ChatScreen
import com.buildstack.promptly.presentation.chat.ChatViewModel
import com.buildstack.promptly.presentation.chat.ChatViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    appContainer: AppContainer,
    onNavigateToSettings: () -> Unit
) {
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(appContainer.chatRepository))
    val chats by homeViewModel.chats.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var currentChatId by remember { mutableStateOf(0L) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Promptly",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider()

                NavigationDrawerItem(
                    label = { Text("New Chat") },
                    selected = currentChatId == 0L,
                    onClick = {
                        currentChatId = 0L
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "Recent Chats",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(chats) { chat ->
                        NavigationDrawerItem(
                            label = { Text(chat.title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                            selected = chat.chatId == currentChatId,
                            onClick = {
                                currentChatId = chat.chatId
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }

                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToSettings()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        val chatViewModel: ChatViewModel = viewModel(
            key = "chat_$currentChatId",
            factory = ChatViewModelFactory(appContainer.chatRepository, appContainer.groqRepository, currentChatId)
        )

        ChatScreen(
            viewModel = chatViewModel,
            onOpenDrawer = { scope.launch { drawerState.open() } }
        )
    }
}
