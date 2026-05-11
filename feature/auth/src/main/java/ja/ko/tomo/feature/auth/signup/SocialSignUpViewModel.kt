package ja.ko.tomo.feature.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
        _uiState.value = SocialSignUpUiState.Success()
    }

    /**
     * 구글 로그인 성공 후 서버에 회원가입 요청
     */
    fun signUpWithGoogle(idToken: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is SocialSignUpUiState.Success) {
                _uiState.update { currentState.copy(isSigngUp = true) }

                when (val result = signUpWithSocialUseCase(provider = "google", idToken = idToken)) {
                    is AuthResult.Success -> {
                        _effect.send(SocialSignUpUiEffect.NavigateToNext)
                    }
                    is AuthResult.Error -> {
                        _effect.send(SocialSignUpUiEffect.ShowToast(result.message))
                    }
                    AuthResult.Empty -> {
                        _effect.send(SocialSignUpUiEffect.ShowSnackbar(R.string.auth_sing_up_empty))
                    }
                }
            }
        }

        _uiState.update {
            if (it is SocialSignUpUiState.Success) it.copy(isSigngUp = false) else it
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _effect.send(SocialSignUpUiEffect.NavigateBack)
        }
    }
}