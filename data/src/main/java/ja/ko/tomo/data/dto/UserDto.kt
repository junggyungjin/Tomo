package ja.ko.tomo.data.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class UserDto(
    val id: String,
    val nickname: String,
    val email: String
)