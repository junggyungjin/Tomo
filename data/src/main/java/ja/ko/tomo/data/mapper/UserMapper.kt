package ja.ko.tomo.data.mapper

import ja.ko.tomo.data.dto.response.UpdateUserProfileResponseDto
import ja.ko.tomo.domain.model.User

fun UpdateUserProfileResponseDto.toDomain(): User {
    return User(
        id = id,
        nickname = nickname,
        nationality = nationality,
        email = null,
        handle = handle,
        profileImageUrl = profileImageUrl,
        status = status,
        introduction = null
    )
}