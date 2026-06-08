package ja.ko.tomo.data.mapper

import ja.ko.tomo.data.dto.response.CallRoomResponseDto
import ja.ko.tomo.data.dto.response.FeedResponseDto
import ja.ko.tomo.data.util.toApiDate
import ja.ko.tomo.domain.feed.model.CallRoom
import ja.ko.tomo.domain.feed.model.Feed
import ja.ko.tomo.domain.feed.model.RoomStatus

fun FeedResponseDto.toDomain(): Feed {
    return Feed(
        id = id,
        content = content,
        authorId = authorId,
        callRoom = callRoom?.toDomain(),
        createdAt = createdAt.toApiDate()
    )
}

fun CallRoomResponseDto.toDomain(): CallRoom {
    return CallRoom(
        id = id,
        status = try {
            RoomStatus.valueOf(status.uppercase())
        }catch (e: Exception) {
            RoomStatus.CLOSED
        },
        maxParticipants = maxParticipants,
        currentParticipants = currentParticipants
    )
}