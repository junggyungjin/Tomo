package ja.ko.tomo.domain.model

/**
 * 채팅방 목록
 */
sealed interface ChatListResult {
    data class Success(val chatRooms: List<ChatRoom>) : ChatListResult
    data object Empty : ChatListResult
    data class Error(val message: String) : ChatListResult
}