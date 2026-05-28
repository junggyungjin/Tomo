package ja.ko.tomo.data.dto.response

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class AuthUserDto(
    val id: String,
    val handle: String,
    val status: String
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class AuthLoginResponseData(
    val accessToken: String,
    val refreshToken: String,
    val user: AuthUserDto,
    val isNewUser: Boolean
)