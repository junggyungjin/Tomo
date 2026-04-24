package ja.ko.tomo.feature.chatroom

import ja.ko.tomo.domain.model.ChatMessage

sealed interface ChatRoomUiState {
    data object Loading : ChatRoomUiState

    data class Success(
        val messages: List<ChatMessage>,
        val inputText: String = "",
        val isSending: Boolean = false
    ) : ChatRoomUiState

    data object Empty : ChatRoomUiState

    data class Error(val message: String) : ChatRoomUiState
}