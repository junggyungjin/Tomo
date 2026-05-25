package ja.ko.tomo.data.dto.request

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class SocialSignUpRequest(
    val token: String,
    val providerId: String,
    val email: String? = null,
    val name: String? = null
) {
}