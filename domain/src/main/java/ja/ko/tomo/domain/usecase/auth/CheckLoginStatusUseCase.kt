package ja.ko.tomo.domain.usecase.auth

import ja.ko.tomo.domain.model.UserResult
import ja.ko.tomo.domain.model.UserSession
import ja.ko.tomo.domain.repository.AuthRepository
import ja.ko.tomo.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 앱 진입 시 사용자의 로그인 상태를 확인
 * @return 로그인 되어있으면 true, 아니면 false
 */
class CheckLoginStatusUseCase @Inject constructor(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository,
    private val userSession: UserSession
) {
    suspend operator fun invoke(): Boolean {
        // 1. 로컬 토큰 유무 확인
        if (!authRepo.isLoggedIn()) return false

        // 2. 서버 검증 API 호출
        // 이때 AuthInterceptor가 자동으로 헤더에 토큰을 실어 보냄
        return when (val result = userRepo.getMyInfo()) {
            is UserResult.Success -> {
                // ACTIVE 상태인 유저만 통과
                if (result.user.status == "ACTIVE") {
                    userSession.setUser(result.user)
                    true
                }else {
                    false
                }
            }
            else -> false // 토큰 만료 또는 서버 에러 시 로그인 화면으로
        }
    }
}