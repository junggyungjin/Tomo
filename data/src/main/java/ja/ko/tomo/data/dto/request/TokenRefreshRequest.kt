package ja.ko.tomo.data.dto.request

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class TokenRefreshRequest(
    val refreshToken: String
)