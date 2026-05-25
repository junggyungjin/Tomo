package ja.ko.tomo.data.mapper

import ja.ko.tomo.data.dto.response.AuthUserDto
import ja.ko.tomo.domain.model.User

fun AuthUserDto.toDomain(): User {
    return User(
        id = id,
        email = "",
        nickname = nickname,
        handle = handle,
        nationality = nationality,
        profileImageUrl = null,
        introduction = ""
    )
}