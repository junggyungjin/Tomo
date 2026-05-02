package ja.ko.tomo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // 앱이 구동될 준비가 되었는지 나타내는 상태 (StateFlow)
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        // 뷰모델이 생성되자마자 초기화 작업을 시작합니다.
        viewModelScope.launch {
            // [현업 실전 예시]
            // 1. 서버에서 필수 설정 데이터 가져오기
            // 2. 로컬 DB 초기화 확인
            // 3. 자동 로그인 토큰 유효성 검사 등
            Timber.e("MainViewModel isReady : ${isReady.value}")

            delay(1500) // 실제 작업 대신 1.5초의 가상 로딩 시간을 줍니다.

            // 모든 작업이 끝나면 준비 완료 신호를 보냅니다.
            _isReady.value = true
        }
    }
}