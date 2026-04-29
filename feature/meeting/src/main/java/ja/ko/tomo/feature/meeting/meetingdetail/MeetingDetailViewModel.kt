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
import ja.ko.tomo.feature.meeting.R
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
                    // UI layer will need to handle localized strings for side effects 
                    // or we could use a resource ID based effect. For now, using hardcoded
                    // strings for effects that come from logic is common if not using a
                    // resource provider in VM, but let's try to be consistent.
                    // Since the effect takes a String, we'd need to change the effect definition
                    // or keep it as is. Let's keep it as is for simplicity if the user didn't ask
                    // to change the architecture, but we'll use the resources we defined.
                    // Actually, the Screen already has access to R.string.
                }

                is MeetingResult.Error -> {
                    _effect.send(MeetingDetailUiEffect.ShowToast(result.message))
                }

                MeetingResult.Empty -> {
                    _uiState.value = MeetingDetailUiState.Error("Meeting not found")
                }
            }
        }
    }

    private fun Meeting.toDetailUiState(): MeetingDetailUiState.Success {
        val buttonTextRes = when {
            isClosed -> R.string.meeting_status_closed
            isJoined -> R.string.meeting_detail_button_cancel
            else -> R.string.meeting_today_button_join
        }

        val isButtonEnabled = !isClosed

        return MeetingDetailUiState.Success(
            meeting = this,
            buttonTextRes = buttonTextRes,
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