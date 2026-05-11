package ja.ko.tomo.data.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class SocialSignUpRequest(
    val provider: String,
    val idToken: String
) {
}