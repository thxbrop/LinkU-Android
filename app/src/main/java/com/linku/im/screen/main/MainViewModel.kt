package com.linku.im.screen.main

import androidx.lifecycle.viewModelScope
import com.linku.data.usecase.ConversationUseCases
import com.linku.data.usecase.OneWordUseCases
import com.linku.domain.Auth
import com.linku.domain.Resource
import com.linku.im.screen.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val conversationUseCases: ConversationUseCases,
    private val oneWordUseCases: OneWordUseCases
) : BaseViewModel<MainState, MainEvent>(MainState()) {
    init {
        onEvent(MainEvent.OneWord)
        Auth.observeCurrent
            .onEach {
                if (it != null) {
                    onEvent(MainEvent.GetConversations)
                } else {
                    getAllConversationsJob?.cancel()
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.CreateConversation -> {}
            MainEvent.OneWord -> fetchOneWord()
            MainEvent.GetConversations -> getAllConversations()
        }
    }

    private var getAllConversationsJob: Job? = null
    private fun getAllConversations() {
        conversationUseCases.observeConversations()
            .onEach { conversations ->
                _state.value = state.value.copy(
                    conversations = conversations.map { it.toMainUI() },
                    loading = false
                )
                getAllConversationsJob?.cancel()
                getAllConversationsJob = viewModelScope.launch {
                    conversations.forEach { conversation ->
                        conversationUseCases.observeLatestContent(conversation.id)
                            .collect { message ->
                                val oldList = state.value.conversations.toMutableList()
                                val oldConversation = oldList.find { it.id == message.cid }
                                if (oldConversation != null) {
                                    oldList.remove(oldConversation)
                                    val copy = oldConversation.copy(
                                        content = message.content
                                    )
                                    oldList.add(copy)
                                } else {
                                    // TODO

                                }
                                _state.value = state.value.copy(
                                    conversations = oldList
                                )
                            }
                    }
                }
            }
            .launchIn(viewModelScope)
        conversationUseCases.fetchConversationsUseCase()
            .onEach { resource ->
                _state.value = when (resource) {
                    Resource.Loading -> state.value.copy(
                        loading = true
                    )
                    is Resource.Success -> state.value.copy(
                        loading = false
                    )
                    is Resource.Failure -> state.value.copy(
                        loading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchOneWord() {
        oneWordUseCases.hitokoto()
            .onEach { resource ->
                _state.value = when (resource) {
                    Resource.Loading -> state.value.copy(
                        drawerTitle = null
                    )
                    is Resource.Success -> state.value.copy(
                        drawerTitle = resource.data.hitokoto
                    )
                    is Resource.Failure -> state.value.copy(
                        drawerTitle = resource.message
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}