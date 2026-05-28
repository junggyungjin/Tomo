package ja.ko.tomo.domain.usecase.auth

import ja.ko.tomo.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 앱 진입 시 사용자의 로그인 상태를 확인
 * @return 로그인 되어있으면 true, 아니면 false
 */
class CheckLoginStatusUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return repo.isLoggedIn()
    }
}