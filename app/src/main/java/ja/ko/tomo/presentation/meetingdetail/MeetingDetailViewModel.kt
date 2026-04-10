package ja.ko.tomo.presentation.meetingdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.domain.model.Meeting
import ja.ko.tomo.domain.model.MeetingDetailResult
import ja.ko.tomo.domain.model.MeetingResult
import ja.ko.tomo.domain.usecase.meeting.CancelJoinUseCase
import ja.ko.tomo.domain.usecase.meeting.GetMeetingDetailUseCase
import ja.ko.tomo.domain.usecase.meeting.JoinMeetingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMeetingDetailUseCase: GetMeetingDetailUseCase,
    private val joinMeetingUseCase: JoinMeetingUseCase,
    private val cancelJoinUseCase: CancelJoinUseCase
) : ViewModel() {

    private val meetingId: Long =
        checkNotNull(savedStateHandle.get<Long>("meetingId"))
    private val _uiState = MutableStateFlow<MeetingDetailUiState>(
        MeetingDetailUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadMeetingDetail()
    }

    private fun loadMeetingDetail() {
        _uiState.value = MeetingDetailUiState.Loading

        viewModelScope.launch {
            when (val result = getMeetingDetailUseCase(meetingId = meetingId)) {
                is MeetingDetailResult.Success -> {
                    _uiState.value = result.meeting.toDetailUiState()
                }

                is MeetingDetailResult.Error -> {
                    _uiState.value = MeetingDetailUiState.Error(result.message)
                }
            }
        }
    }

    fun onActionButtonClick() {
        val currentState = _uiState.value as? MeetingDetailUiState.Success ?: return
        val meeting = currentState.meeting

        viewModelScope.launch {
            val result = if (meeting.isJoined) {
                cancelJoinUseCase(meeting.id)
            }else {
                joinMeetingUseCase(meeting.id)
            }

            when (result) {
                is MeetingResult.Success -> {
                    _uiState.value = result.meeting.toDetailUiState()
                }

                is MeetingResult.Error -> {
                    _uiState.value = MeetingDetailUiState.Error(result.message)
                }

                MeetingResult.Empty -> {
                    _uiState.value = MeetingDetailUiState.Error("모임 정보를 찾을 수 없습니다")
                }
            }
        }
    }

    private fun Meeting.toDetailUiState(): MeetingDetailUiState.Success {
        val buttonText = when {
            isClosed -> "마감됨"
            isJoined -> "참가 취소"
            else -> "참가하기"
        }

        val isButtonEnabled = !isClosed

        return MeetingDetailUiState.Success(
            meeting = this,
            buttonText = buttonText,
            isButtonEnabled = isButtonEnabled
        )
    }
}