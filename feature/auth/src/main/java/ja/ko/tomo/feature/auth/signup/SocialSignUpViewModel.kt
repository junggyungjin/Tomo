package ja.ko.tomo.feature.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.domain.model.AuthResult
import ja.ko.tomo.domain.usecase.auth.SignUpWithSocialUseCase
import ja.ko.tomo.feature.auth.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialSignUpViewModel @Inject constructor(
    private val signUpWithSocialUseCase: SignUpWithSocialUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SocialSignUpUiState>(SocialSignUpUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SocialSignUpUiEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        initScreen()
    }

    /**
     * // 화면 초기화 및 재시도 로직
     */
    fun initScreen() {
        _uiState.value = SocialSignUpUiState.Loading
        try {
            // 예: 서버에서 소셜 가입 설정(약관 등)을 가져온다고 가정
            // val config = getSignUpConfigUseCase()
            _uiState.value = SocialSignUpUiState.Success()
        }catch (e: Exception) {
            handleError(e)
        }
    }

    /**
     * 구글 로그인 성공 후 서버에 회원가입 요청
     */
    fun signUpWithGoogle(
        token: String,
        providerId: String,
        email: String?,
        name: String?
    ) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is SocialSignUpUiState.Success) {
                _uiState.update { currentState.copy(isSigningUp = true) }

                try {
                    when (val result = signUpWithSocialUseCase(
                        provider = "google",
                        token = token,
                        providerId = providerId,
                        email = email,
                        name = name
                    )
                    ) {
                        is AuthResult.Success -> {
                            _effect.send(SocialSignUpUiEffect.NavigateToNext)
                        }

                        is AuthResult.Error -> {
                            _effect.send(SocialSignUpUiEffect.ShowToast(result.message))
                        }

                        AuthResult.Empty -> {
                            _effect.send(SocialSignUpUiEffect.ShowSnackbar(UiText.StringResource(R.string.auth_sing_up_empty)))
                        }
                    }
                } catch (e: Exception) {
                    // 예상치 못한 시스템 런타임 에러 발생 시 처리
                    // 가입 중에 화면을 아예 에러 뷰로 바꿔버릴지, 아니면 Snackbar를 띄울지 선택
                    handleError(e)
                } finally {
                    _uiState.update {
                        if (it is SocialSignUpUiState.Success) it.copy(isSigningUp = false) else it
                    }
                }
            }
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _effect.send(SocialSignUpUiEffect.NavigateBack)
        }
    }

    /**
     * 전체 화면 에러 핸들 처리 용도
     */
    private fun handleError(exception: Exception) {
        _uiState.value = SocialSignUpUiState.Error(
            message = UiText.DynamicString(exception.localizedMessage ?: "UnKnown Error")
        )
    }
}