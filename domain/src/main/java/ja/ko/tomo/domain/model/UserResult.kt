package ja.ko.tomo.domain.model

sealed interface UserResult {
    data class Success(val user: User) : UserResult
    data object Empty : UserResult
    data class Error(val message: String) : UserResult
}