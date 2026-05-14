package ja.ko.tomo.data.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto? = null
)