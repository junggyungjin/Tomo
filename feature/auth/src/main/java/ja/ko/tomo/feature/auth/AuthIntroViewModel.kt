package ja.ko.tomo.feature.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.core.ui.base.BaseViewModel
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.domain.model.AuthResult
import ja.ko.tomo.domain.usecase.auth.DevLoginUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthIntroViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val devLoginUseCase: DevLoginUseCase
): BaseViewModel<AuthIntroUiState, AuthIntroUiEffect>(AuthIntroUiState.Loading) {

    // 유저가 문의하기를 눌러서 앱을 나갔는지 추적
    private var isInquiryPending: Boolean
        get() = savedStateHandle.get<Boolean>("is_inquiry_pending") ?: false
        set(value) = savedStateHandle.set("is_inquiry_pending", value)

    init {
        initScreen()
    }

    /**
     * 화면 초기화 및 재시도 로직
     */
    fun initScreen() {
        viewModelScope.launch {
            _uiState.value = AuthIntroUiState.Loading
            try {
                // TODO 추후 여기서 서버 공지사항 등을 체크하겠지만,
                // TODO 현재는 바로 Success로 전환합니다.
                _uiState.value = AuthIntroUiState.Success()
            }catch (e: Exception) {
                handleError(e) { AuthIntroUiState.Error(it) }
            }
        }
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _uiEffect.send(AuthIntroUiEffect.NavigateToSignUp)
        }
    }

    fun onInquiryClick() {
        viewModelScope.launch {
            isInquiryPending = true
            _uiEffect.send(AuthIntroUiEffect.NavigateToInquiry)
        }
    }

    // 앱으로 돌아왔을때 Screen에서 호출
    fun onUserReturned() {
        if (isInquiryPending) {
            viewModelScope.launch {
                _uiEffect.send(AuthIntroUiEffect.ShowSnackbar(
                    UiText.StringResource(R.string.auth_intro_inquiry_email_send_completed)
                ))
                isInquiryPending = false
            }
        }
    }

    /**
     * 개발 환경 전용 로그인 (Intent)
     */
    fun onDevLoginClick() {
        val currentState = _uiState.value as? AuthIntroUiState.Success ?: return
        if (currentState.isDevLogginIn) return

        viewModelScope.launch {
            _uiState.update { currentState.copy(isDevLogginIn = true) }

            when (val result = devLoginUseCase("dev-tester-1")) {
                is AuthResult.Success -> {
                    _uiEffect.send(AuthIntroUiEffect.NavigateToHome)
                }
                is AuthResult.Error -> {
                    _uiState.update { currentState.copy(isDevLogginIn = false) }
                    _uiEffect.send(AuthIntroUiEffect.ShowSnackbar(UiText.DynamicString(result.message)))
                }
                else -> {
                    _uiState.update { currentState.copy(isDevLogginIn = false) }
                }
            }
        }
    }
}