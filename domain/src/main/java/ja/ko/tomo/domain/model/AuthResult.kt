package ja.ko.tomo.domain.model

sealed interface AuthResult {
    data class Success(val user: User) : AuthResult
    data object LogoutSuccess : AuthResult
    data object Empty : AuthResult
    data class Error(val message: String) : AuthResult
}