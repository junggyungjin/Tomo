package ja.ko.tomo.feature.chat.chatroom

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.domain.model.ChatMessageListResult
import ja.ko.tomo.domain.model.ChatMessageResult
import ja.ko.tomo.domain.usecase.chat.GetMessagesUseCase
import ja.ko.tomo.domain.usecase.chat.MarkAsReadUseCase
import ja.ko.tomo.domain.usecase.chat.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase
) : ViewModel() {

    private val chatId: Long = checkNotNull(savedStateHandle.get<Long>("chatId"))

    private val _uiState = MutableStateFlow<ChatRoomUiState>(ChatRoomUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        observeMessages()
        markAsRead()
    }

    private fun observeMessages() {
        viewModelScope.launch {
            getMessagesUseCase(chatId).collect { result ->
                _uiState.value = when (result) {
                    is ChatMessageListResult.Success -> ChatRoomUiState.Success(messages = result.messages)
                    ChatMessageListResult.Empty -> ChatRoomUiState.Success(messages = emptyList())
                    is ChatMessageListResult.Error -> ChatRoomUiState.Error(result.message)
                }
            }
        }
    }

    private fun markAsRead() {
        viewModelScope.launch {
            markAsReadUseCase(chatId)
        }
    }

    fun onMessageChange(text: String) {
        val currentState = _uiState.value as? ChatRoomUiState.Success ?: return
        _uiState.value = currentState.copy(inputText = text)
    }

    fun sendMessage() {
        val currentState = _uiState.value as? ChatRoomUiState.Success ?: return
        val messageToSend = currentState.inputText
        if (messageToSend.isBlank()) return

        _uiState.value = currentState.copy(isSending = true)

        viewModelScope.launch {
            val result = sendMessageUseCase(chatId, messageToSend)
            if (result is ChatMessageResult.Success) {
                // 전송 성공 시 입력창 바꾸기
                _uiState.update {
                    (it as ChatRoomUiState.Success).copy(inputText = "", isSending = false)
                }
            } else {
                // 실패
                _uiState.update { (it as ChatRoomUiState.Success).copy(isSending = false) }
            }
        }

    }
}