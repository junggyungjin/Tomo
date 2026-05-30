package ja.ko.tomo.data.dto.response

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class TokenRefreshResponseDto(
    val accessToken: String,
    val refreshToken: String
) {
}