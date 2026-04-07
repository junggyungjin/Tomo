package ja.ko.tomo.presentation.meeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ja.ko.tomo.domain.model.Meeting
import ja.ko.tomo.domain.model.MeetingResult
import ja.ko.tomo.domain.usecase.meeting.GetTodayMeetingUseCase
import ja.ko.tomo.domain.usecase.meeting.JoinMeetingUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class TomoMeetingViewModel(
    private val getTodayMeetingUseCase: GetTodayMeetingUseCase,
    private val joinMeetingUseCase: JoinMeetingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TomoMeetingUiState>(
        TomoMeetingUiState.Loading
    )
    val uiState: StateFlow<TomoMeetingUiState> = _uiState

    private val _event = MutableSharedFlow<TomoMeetingEvent>()
    val event: SharedFlow<TomoMeetingEvent> = _event.asSharedFlow()

    init {
        loadMeeting()
    }

    private fun loadMeeting() {
        _uiState.value = TomoMeetingUiState.Loading

        viewModelScope.launch {
            when(val result = getTodayMeetingUseCase()) {
                is MeetingResult.Success -> {
                    _uiState.value = result.meeting.toSuccessUiState()
                }

                MeetingResult.Empty -> {
                    _uiState.value = TomoMeetingUiState.Empty
                }

                is MeetingResult.Error -> {
                    _uiState.value = TomoMeetingUiState.Error(result.message)
                }
            }
        }
    }

    fun onJoinClick() {
        val currentState = _uiState.value
        if (currentState !is TomoMeetingUiState.Success) return

        viewModelScope.launch {
            when (val result = joinMeetingUseCase(currentState.meeting.id)) {
                is MeetingResult.Success -> {
                    _uiState.value = result.meeting.toSuccessUiState()
                    _event.emit(TomoMeetingEvent.ShowToast("참가 완료"))
                }

                MeetingResult.Empty -> {
                    _uiState.value = TomoMeetingUiState.Empty
                }

                is MeetingResult.Error -> {
                    _event.emit(TomoMeetingEvent.ShowToast(result.message))
                }
            }
        }
    }

    private fun Meeting.toSuccessUiState(): TomoMeetingUiState.Success {
        return TomoMeetingUiState.Success(
            meeting = this,
            buttonText = if (isClosed) "마감됨" else "참가하기",
            isJoinEnabled = !isClosed
        )
    }
}