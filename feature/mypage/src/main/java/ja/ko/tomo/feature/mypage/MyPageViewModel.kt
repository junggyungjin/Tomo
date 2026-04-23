package ja.ko.tomo.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.domain.model.UserResult
import ja.ko.tomo.domain.usecase.user.GetUserInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadMyInfo()
    }

    private fun loadMyInfo() {
        viewModelScope.launch {
            when(val result = getUserInfoUseCase()) {
                is UserResult.Success -> {
                    _uiState.value = MyPageUiState.Success(result.user)
                }
                UserResult.Empty -> {
                    _uiState.value = MyPageUiState.Error("유저 정보가 없습니다.")
                }
                is UserResult.Error -> {
                    _uiState.value = MyPageUiState.Error(result.message)
                }
            }
        }
    }
}