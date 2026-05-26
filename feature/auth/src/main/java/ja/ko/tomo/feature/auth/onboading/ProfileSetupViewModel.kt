package ja.ko.tomo.feature.auth.onboading

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.domain.model.Gender
import ja.ko.tomo.domain.model.UserResult
import ja.ko.tomo.domain.usecase.user.UpdateUserProfileUseCase
import ja.ko.tomo.feature.auth.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileSetupUiState>(ProfileSetupUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<ProfileSetupUiEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        initScreen()
    }

    fun initScreen() {
        _uiState.value = ProfileSetupUiState.Loading
        try {
            _uiState.value = ProfileSetupUiState.Success()
        }catch (e: Exception) {
            handleError(e)
        }
    }

    fun onNicknameChange(nickname: String) {
        _uiState.update {
            if (it is ProfileSetupUiState.Success) it.copy(nickname = nickname, nicknameError = null) else it
        }
    }

    fun onGenderSelect(gender: Gender) {
        _uiState.update { if (it is ProfileSetupUiState.Success) it.copy(gender = gender) else it }
    }

    fun onNationalitySelect(nationality: String) {
        _uiState.update { if (it is ProfileSetupUiState.Success) it.copy(nationality = nationality) else it }
    }

    fun onImageSelected(uri: Uri) {
        _uiState.update {
            if (it is ProfileSetupUiState.Success) it.copy(selectedImageUri = uri) else it
        }
    }

    fun onCompleteClick(userId: String) {
        val currentState = _uiState.value as? ProfileSetupUiState.Success ?: return

        // 간단한 유효성 검사
        if (currentState.nickname.length < 2) {
            _uiState.update {
                if (it is ProfileSetupUiState.Success) {
                    it.copy(nicknameError = UiText.StringResource(R.string.profile_setup_error_nickname_too_short))
                }else it
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                if (it is ProfileSetupUiState.Success) {
                    it.copy(isSubmitting = true)
                }else it
            }

            val result = updateUserProfileUseCase(
                userId = userId,
                nickname = currentState.nickname,
                nationality = currentState.nationality,
                gender = currentState.gender ?: Gender.OTHER,
                profileImageUrl = currentState.profileImageUrl
            )

            when (result) {
                is UserResult.Success -> {
                    _effect.send(ProfileSetupUiEffect.NavigateToHome)
                }
                is UserResult.Error -> {
                    _effect.send(ProfileSetupUiEffect.ShowSnackbar(UiText.DynamicString(result.message)))
                }
                UserResult.Empty -> {}
            }
            _uiState.update {
                if (it is ProfileSetupUiState.Success) {
                    it.copy(isSubmitting = false)
                }else it
            }
        }
    }

    /**
     * 전체 화면 에러 핸들 처리 용도
     */
    private fun handleError(exception: Exception) {
        _uiState.value = ProfileSetupUiState.Error(
            message = UiText.DynamicString(exception.localizedMessage ?: "UnKnown Error")
        )
    }
}