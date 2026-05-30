package ja.ko.tomo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.data.local.AuthEvent
import ja.ko.tomo.data.local.AuthEventHelper
import ja.ko.tomo.domain.usecase.auth.CheckLoginStatusUseCase
import ja.ko.tomo.domain.usecase.auth.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase, // 로그인 상태 체크 유스케이스 주입
    authEventHelper: AuthEventHelper,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    // 앱이 구동될 준비가 되었는지 나타내는 상태 (StateFlow)
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _startDestination = MutableStateFlow<String>(TomoNavRoutes.AuthIntro)
    val startDestination = _startDestination.asStateFlow()

    // 전역 인증 이벤트를 UI에서 구독할 수 있도록 노출
    val authEvent: SharedFlow<AuthEvent> = authEventHelper.authEvent

    init {
        // 뷰모델이 생성되자마자 초기화 작업을 시작합니다.

        // [현업 실전 예시]
        // 1. 서버에서 필수 설정 데이터 가져오기
        // 2. 로컬 DB 초기화 확인
        // 3. 자동 로그인 토큰 유효성 검사 등

        checkLoginStatus()
    }

    /**
     * 자동 로그인 여부를 확인하여 시작 화면을 결정
     */
    private fun checkLoginStatus() {
        viewModelScope.launch {
            // [현업 실전 예시]
            // 1. 서버에서 필수 설정 데이터 가져오기
            // 2. 로컬 DB 초기화 확인
            // 3. 자동 로그인 토큰 유효성 검사 등
            Timber.e("MainViewModel isReady : ${isReady.value}")

            try {
                val isLoggedIn = checkLoginStatusUseCase()

                if (isLoggedIn) {
                    // 성공 시 홈 화면 이동
                    _startDestination.value = TomoNavRoutes.MeetingList
                    Timber.tag("MainViewModel").d("자동 로그인 성공: MeetingList로 이동")
                }else {
                    // 검증 실패 시 로컬 데이터를 초기화하여 다음 진입 시 불필요한 검증 방지
                    logoutUseCase()
                    _startDestination.value = TomoNavRoutes.AuthIntro
                    Timber.tag("MainViewModel").d("자동 로그인 실패: 로컬 데이터 초기화 및 AuthIntro 유지")
                }
            }catch (e: Exception) {
                // 에러 발생 시 기본값(로그인 화면) 유지
                logoutUseCase()
                _startDestination.value = TomoNavRoutes.AuthIntro
                Timber.e(e, "로그인 상태 확인 중 에러 발생")
            }finally {
                // 모든 작업이 끝나면 준비 완료 신호를 보냅니다.
                _isReady.value = true
            }

        }
    }

    /**
     * 세션 만료 시 호출될 로그아웃 처리
     */
    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}