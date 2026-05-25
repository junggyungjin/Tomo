package ja.ko.tomo.data.mapper

import ja.ko.tomo.data.dto.response.UpdateUserProfileResponseDto
import ja.ko.tomo.domain.model.User

fun UpdateUserProfileResponseDto.toDomain(): User {
    return User(
        id = this.id,
        nickname = this.nickname,
        nationality = this.nationality,
        email = null,
        handle = null,
        profileImageUrl = null,
        introduction = null
    )
}