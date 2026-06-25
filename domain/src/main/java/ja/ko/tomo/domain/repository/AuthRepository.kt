package ja.ko.tomo.domain.repository

import ja.ko.tomo.domain.model.AuthResult

interface AuthRepository {
    suspend fun signUpWithSocial(
        provider: String,
        token: String,
        providerId: String,
        email: String?,
        name: String?
    ): AuthResult

    suspend fun isLoggedIn(): Boolean

    suspend fun logout(): AuthResult

    /**
     * 개발 환경 전용 로그인
     * @param providerId 테스트용 ID
     */
    suspend fun devLogin(providerId: String): AuthResult
}