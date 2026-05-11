package ja.ko.tomo.data.mapper

import ja.ko.tomo.data.dto.UserDto
import ja.ko.tomo.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id.toLongOrNull() ?: 0L,
        email = email,
        nickname = nickname,
        profileImageUrl = null,
        introduction = ""
    )
}