package ja.ko.tomo.domain.model

/**
 * 단일 채팅
 */
sealed interface ChatMessageResult {
    data class Success(val message: ChatMessage) : ChatMessageResult
    data class Error(val message: String) : ChatMessageResult
}