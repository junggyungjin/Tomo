package ja.ko.tomo.presentation.meetingdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ja.ko.tomo.domain.usecase.meeting.CancelJoinUseCase
import ja.ko.tomo.domain.usecase.meeting.GetMeetingDetailUseCase
import ja.ko.tomo.domain.usecase.meeting.JoinMeetingUseCase

class MeetingDetailViewModelFactory(
    private val meetingId: Long,
    private val getMeetingDetailUseCase: GetMeetingDetailUseCase,
    private val joinMeetingUseCase: JoinMeetingUseCase,
    private val cancelJoinUseCase: CancelJoinUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeetingDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeetingDetailViewModel(
                meetingId = meetingId,
                getMeetingDetailUseCase = getMeetingDetailUseCase,
                joinMeetingUseCase = joinMeetingUseCase,
                cancelJoinUseCase = cancelJoinUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}