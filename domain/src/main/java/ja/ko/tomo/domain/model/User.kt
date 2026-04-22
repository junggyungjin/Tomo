package ja.ko.tomo.domain.model

data class User(
    val id: Long,
    val email: String,
    val nickname: String,
    val profileImageUrl: String?,
    val introduction: String
)