package ja.ko.tomo.feature.meeting.meetingcreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.domain.model.MeetingResult
import ja.ko.tomo.domain.usecase.meeting.CreateMeetingUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingCreateViewModel @Inject constructor(
    private val createMeetingUseCase: CreateMeetingUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<MeetingCreateUiState>(
        MeetingCreateUiState.Success()
    )
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<MeetingCreateUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun saveMeeting() {
        val currentState = _uiState.value as? MeetingCreateUiState.Success ?: return

        _uiState.value = MeetingCreateUiState.Loading

        viewModelScope.launch {
            val result = createMeetingUseCase(
                title = currentState.title,
                subtitle = currentState.subtitle,
                dateTime = currentState.dateTime,
                location = currentState.location,
                capacity = currentState.capacity.toIntOrNull() ?: 0
            )

            when (result) {
                is MeetingResult.Success -> {
                    _effect.apply {
                        send(MeetingCreateUiEffect.ShowSnackbar("모임 생성이 완료되었습니다!"))
                        send(MeetingCreateUiEffect.NavigateBack)
                    }
                }
                is MeetingResult.Error -> {
                    _effect.send(MeetingCreateUiEffect.ShowSnackbar(result.message))
                    _uiState.value = currentState // 이전 상태로 복구
                }
                else -> {}
            }
        }
    }

    // 제목 변경 시 호출
    fun onTitleChanged(title: String) {
        updateSuccessState { it.copy(title = title) }
    }

    fun onSubtitleChanged(subtitle: String) {
        updateSuccessState { it.copy(subtitle = subtitle) }
    }

    fun onDateTimeChanged(dateTime: String) {
        updateSuccessState { it.copy(dateTime = dateTime) }
    }

    fun onLocationChanged(location: String) {
        updateSuccessState { it.copy(location = location) }
    }

    fun onCapacityChanged(capacity: String) {
        if (capacity.all { it.isDigit() }) {
            updateSuccessState { it.copy(capacity = capacity) }
        }
    }

    // TODO 질문 1 update: (MeetingCreateUiState.Success) -> MeetingCreateUiState.Success는 무슨 의미야
    private fun updateSuccessState(update: (MeetingCreateUiState.Success) -> MeetingCreateUiState.Success) {
        val currentState = _uiState.value as? MeetingCreateUiState.Success ?: return
        val updated = update(currentState)

        _uiState.value = updated.copy(
            isSaveEnabled = validate(updated)
        )
    }

    // 유효성 검사
    private fun validate(state: MeetingCreateUiState.Success): Boolean {
        return state.title.isNotBlank() &&
                state.subtitle.isNotBlank() &&
                state.dateTime.isNotBlank() &&
                state.location.isNotBlank() &&
                state.capacity.toIntOrNull() != null
    }
}