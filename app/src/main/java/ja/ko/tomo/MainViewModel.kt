package ja.ko.tomo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.domain.usecase.auth.CheckLoginStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase // 로그인 상태 체크 유스케이스 주입
) : ViewModel() {

    // 앱이 구동될 준비가 되었는지 나타내는 상태 (StateFlow)
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _startDestination = MutableStateFlow<String>(TomoNavRoutes.AuthIntro)
    val startDestination = _startDestination.asStateFlow()

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
                    _startDestination.value = TomoNavRoutes.MeetingList
                }
            }catch (e: Exception) {
                // 에러 발생 시 기본값(로그인 화면) 유지
            }finally {
                // 모든 작업이 끝나면 준비 완료 신호를 보냅니다.
                _isReady.value = true
            }

        }
    }
}