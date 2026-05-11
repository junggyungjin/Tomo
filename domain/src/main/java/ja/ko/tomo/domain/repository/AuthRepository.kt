package ja.ko.tomo.domain.repository

import ja.ko.tomo.domain.model.AuthResult

interface AuthRepository {
    suspend fun signUpWithSocial(
        provider: String,
        idToken: String
    ): AuthResult
}