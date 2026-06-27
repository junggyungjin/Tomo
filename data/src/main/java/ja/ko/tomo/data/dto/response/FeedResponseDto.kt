package ja.ko.tomo.data.dto.response

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

/**
 * 피드 정보 응답 DTO
 */
@OptIn(InternalSerializationApi::class)
@Serializable
data class FeedResponseDto(
    val id: String,
    val content: String? = null,
    val authorId: String,
    val authorNickname: String,
    val authorHandle: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val isAuthorFollowing: Boolean,
    val callRoom: CallRoomResponseDto? = null,
    val createdAt: String
) {
}

/**
 * 피드 내 통화방 정보 응답 DTO
 */
@OptIn(InternalSerializationApi::class)
@Serializable
data class CallRoomResponseDto(
    val id: String,
    val status: String, // "OPEN","CLOSED"
    val maxParticipants: Int,
    val currentParticipants: Int
)