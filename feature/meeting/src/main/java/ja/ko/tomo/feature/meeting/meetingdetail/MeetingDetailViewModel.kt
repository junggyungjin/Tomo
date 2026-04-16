package ja.ko.tomo.feature.meeting.meetingdetail

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
import ja.ko.tomo.domain.usecase.meeting.ToggleFavoriteUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMeetingDetailUseCase: GetMeetingDetailUseCase,
    private val joinMeetingUseCase: JoinMeetingUseCase,
    private val cancelJoinUseCase: CancelJoinUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val meetingId: Long =
        checkNotNull(savedStateHandle.get<Long>("meetingId"))
    private val _uiState = MutableStateFlow<MeetingDetailUiState>(
        MeetingDetailUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<MeetingDetailUiEffect>()
    val effect = _effect.receiveAsFlow()

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
                    val msg = if (result.meeting.isJoined) "참가가 완료되었습니다!" else "참가가 취소되었습니다"
                    _effect.send(MeetingDetailUiEffect.ShowSnackbar(msg))
                    _effect.send(MeetingDetailUiEffect.NavigateBack)
                }

                is MeetingResult.Error -> {
                    _effect.send(MeetingDetailUiEffect.ShowToast(result.message))
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

    fun toggleFavorite(meetingId: Long) {
        viewModelScope.launch {
            val result = toggleFavoriteUseCase(meetingId = meetingId)
            if (result is MeetingResult.Success) {
                loadMeetingDetail()
            }
        }
    }
}