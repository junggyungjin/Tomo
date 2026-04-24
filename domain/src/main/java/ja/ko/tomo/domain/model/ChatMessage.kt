package ja.ko.tomo.domain.model

data class ChatMessage(
    val id: Long,
    val senderNickname: String,
    val message: String,
    val sentTime: String,
    val isMine: Boolean
)