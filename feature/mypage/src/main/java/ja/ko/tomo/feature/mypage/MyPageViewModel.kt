package ja.ko.tomo.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.core.ui.base.BaseViewModel
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.domain.model.AuthResult
import ja.ko.tomo.domain.model.UserResult
import ja.ko.tomo.domain.usecase.auth.LogoutUseCase
import ja.ko.tomo.domain.usecase.user.GetUserInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<MyPageUiState, MyPageUiEffect>(MyPageUiState.Loading) {

    init {
        loadMyInfo()
    }

    fun loadMyInfo() {
        viewModelScope.launch {
            when(val result = getUserInfoUseCase()) {
                is UserResult.Success -> {
                    _uiState.value = MyPageUiState.Success(result.user)
                }
                UserResult.Empty -> {
                    _uiState.value = MyPageUiState.Error(UiText.StringResource(R.string.mypage_error_no_user))
                }
                is UserResult.Error -> {
                    val errorMsg = UiText.DynamicString(result.message)
                    _uiState.value = MyPageUiState.Error(errorMsg)
                    _uiEffect.send(MyPageUiEffect.ShowSnackBar(errorMsg))
                }
            }
        }
    }

    /**
     * 로그아웃 버튼 클릭 (Intent)
     */
    fun onLogoutClick() {
        val currentState = uiState.value as? MyPageUiState.Success ?: return
        if (currentState.isLoggingOut) return

        viewModelScope.launch {
            _uiState.update { currentState.copy(isLoggingOut = true) }

            when (val result = logoutUseCase()) {
                is AuthResult.LogoutSuccess -> {
                    _uiEffect.send(MyPageUiEffect.NavigateToAuth)
                }
                is AuthResult.Error -> {
                    _uiState.update { currentState.copy(isLoggingOut = false) }
                    _uiEffect.send(MyPageUiEffect.ShowSnackBar(UiText.DynamicString(result.message)))
                }
                else -> {
                    _uiState.update { currentState.copy(isLoggingOut = false) }
                }
            }
        }
    }
}