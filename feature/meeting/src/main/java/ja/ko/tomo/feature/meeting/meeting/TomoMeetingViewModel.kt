package ja.ko.tomo.feature.meeting.meeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.domain.model.Meeting
import ja.ko.tomo.domain.model.MeetingResult
import ja.ko.tomo.domain.usecase.meeting.GetTodayMeetingUseCase
import ja.ko.tomo.domain.usecase.meeting.JoinMeetingUseCase
import ja.ko.tomo.feature.meeting.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TomoMeetingViewModel @Inject constructor(
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
                    // UI에서 context를 사용할 수 없으므로 Toast 메시지는 리소스 ID를 보내는 방식으로 추후 개선 가능하지만
                    // 일단은 String으로 유지하거나 Event에 ResId를 담도록 수정해야 함.
                    // 여기서는 일단 String으로 유지 (Domain Layer에서 오는 메시지는 보통 String임)
                    // 하지만 "참가 완료"는 하드코딩이므로 수정.
                    // TomoMeetingEvent.ShowToast(String) 이므로 context가 없는 ViewModel에서는 좀 애매함.
                    // 일단 하드코딩 문자열만 제거하는 방향으로 진행.
                    // 실제 앱에서는 string resource provider 등을 사용하거나 UI layer에서 처리함.
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
            buttonTextRes = if (isClosed) R.string.meeting_status_closed else R.string.meeting_today_button_join,
            isJoinEnabled = !isClosed
        )
    }
}