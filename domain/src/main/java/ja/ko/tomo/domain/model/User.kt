package ja.ko.tomo.domain.model

data class User(
    val id: String,
    val email: String?,
    val nickname: String?,
    val handle: String?,
    val nationality: String?,
    val profileImageUrl: String?,
    val status: String,
    val introduction: String?
)