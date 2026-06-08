package ja.ko.tomo.domain.feed.model

import java.util.Date

data class Feed(
    val id: String,
    val content: String?,
    val authorId: String,
    val callRoom: CallRoom?,
    val createdAt: Date
)

data class CallRoom(
    val id: String,
    val status: RoomStatus,
    val maxParticipants: Int,
    val currentParticipants: Int
)

enum class RoomStatus {
    OPEN, CLOSED
}