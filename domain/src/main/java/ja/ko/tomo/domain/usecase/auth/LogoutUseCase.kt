package ja.ko.tomo.domain.usecase.auth

import ja.ko.tomo.domain.model.AuthResult
import ja.ko.tomo.domain.model.UserSession
import ja.ko.tomo.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepo: AuthRepository,
    private val userSession: UserSession
) {
    suspend operator fun invoke(): AuthResult {
        val result = authRepo.logout() // 토큰 삭제
        userSession.clear() // 메모리 데이터 삭제
        return result
    }
}