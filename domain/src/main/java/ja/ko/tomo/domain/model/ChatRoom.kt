package ja.ko.tomo.domain.model

data class ChatRoom(
    val id: Long, // 채팅방 id (보통 모임 id와 동일하게 갈 수 있음)
    val meetingId: Long, // 연결된 모임 ID
    val title: String, //채팅방 이름
    val lastMessage: String, // 마지막으로 주고받은 메세지
    val lastMessageTime: String, // 마지막 메세지 시간
    val unreadCount: Int // 읽지 않은 메시지 수
)