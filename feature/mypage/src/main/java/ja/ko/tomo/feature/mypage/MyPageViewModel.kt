package ja.ko.tomo.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
            val user = getUserInfoUseCase()
            if (user != null) {
                _uiState.value = MyPageUiState.Success(user = user)
            }else {
                _uiState.value = MyPageUiState.Error("유저 정보를 불러올 수 없습니다.")
            }
        }
    }
}