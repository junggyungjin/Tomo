package ja.ko.tomo.feature.chat.chatlist

import ja.ko.tomo.domain.model.ChatRoom

sealed interface ChatListUiState {
    data object Loading : ChatListUiState
    data class Success(val chatRooms: List<ChatRoom>) : ChatListUiState
    data object Empty : ChatListUiState
    data class Error(val message: String) : ChatListUiState
}