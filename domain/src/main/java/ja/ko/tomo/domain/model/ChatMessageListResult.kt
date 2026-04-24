package ja.ko.tomo.domain.model

/**
 * 채팅방 안의 채팅들
 */
sealed interface ChatMessageListResult {
    data class Success(val messages: List<ChatMessage>) : ChatMessageListResult
    data object Empty : ChatMessageListResult
    data class Error(val message: String) : ChatMessageListResult
}