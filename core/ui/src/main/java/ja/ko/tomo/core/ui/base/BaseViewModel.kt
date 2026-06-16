package ja.ko.tomo.core.ui.base

import androidx.lifecycle.ViewModel
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.core.ui.util.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * 모든 viewModel의 근간이 되는 Base 클래스
 * @param S UiState 타입
 * @param E UiEffect 타입
 */
abstract class BaseViewModel<S, E>(initialState: S) : ViewModel() {
    // 공통 상태 관리
    protected val _uiState = MutableStateFlow<S>(initialState)
    val uiState = _uiState.asStateFlow()

    // 공통 이펙트 관리
    protected val _uiEffect = Channel<E>()
    val uiEffect = _uiEffect.receiveAsFlow()

    /**
     * 공통 에러 처리 함수
     * @param throwable 발생한 에러
     * @param onErrorState 에러 상태로 변경하기 위한 람다 (각 ViewModel에서 구현)
     */
    protected fun handleError(throwable: Throwable, onErrorState: (UiText) -> S) {
        val errorUiText = throwable.toUiText()
        _uiState.value = onErrorState(errorUiText)
    }
}