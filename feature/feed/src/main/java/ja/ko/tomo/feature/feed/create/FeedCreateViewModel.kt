package ja.ko.tomo.feature.feed.create

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.core.ui.base.BaseViewModel
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.domain.feed.model.FeedResult
import ja.ko.tomo.domain.feed.usecase.CreateFeedUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 피드 생성을 담당하는 ViewModel
 */
@HiltViewModel
class FeedCreateViewModel @Inject constructor(
    private val createFeedUseCase: CreateFeedUseCase
) : BaseViewModel<FeedCreateUiState, FeedCreateUiEffect>(FeedCreateUiState.Loading) {

    init {
        initScreen()
    }

    // Intent : 화면 초기화 및 재시도 로직
    fun initScreen() {
        _uiState.value = FeedCreateUiState.Loading
        try {
            _uiState.value = FeedCreateUiState.Success()
        }catch (e: Exception) {
            handleError(e) { FeedCreateUiState.Error(it) }
        }
    }

    // Intent : 리프레시
    fun onRefresh() {
        initScreen()
    }

    // Intent : 피드 내용 입력
    fun onContentChange(content: String) {
        val currentState = _uiState.value
        if (currentState is FeedCreateUiState.Success) {
            _uiState.update { currentState.copy(content = content) }
        }
    }

    // Intent : 통화방 생성 여부 토글
    fun onHasCallRoomChange(hasCallRoom: Boolean) {
        val currentState = _uiState.value
        if (currentState is FeedCreateUiState.Success) {
            _uiState.update { currentState.copy(hasCallRoom = hasCallRoom) }
        }
    }

    // Intent: 피드 등록 버튼 클릭
    fun onCreateClick() {
        val currentState = _uiState.value as? FeedCreateUiState.Success ?: return

        if (!currentState.canSubmit) return

        viewModelScope.launch {
            // 로딩 상태 시작
            _uiState.update { currentState.copy(isSubmitting = true) }

            when (val result = createFeedUseCase(currentState.content, currentState.hasCallRoom)) {
                is FeedResult.SingleSuccess -> {
                    _uiEffect.send(FeedCreateUiEffect.NavigateBack(isSuccess = true))
                }
                is FeedResult.Error -> {
                    _uiState.update { currentState.copy(isSubmitting = false) }
                    _uiEffect.send(FeedCreateUiEffect.ShowSnackBar(UiText.DynamicString(result.message)))
                }
                else -> {
                    _uiState.update { currentState.copy(isSubmitting = false) }
                }
            }
        }
    }

    // Intent: 뒤로 가기 클릭
    fun onBackClick() {
        viewModelScope.launch {
            _uiEffect.send(FeedCreateUiEffect.NavigateBack(isSuccess = false))
        }
    }

}