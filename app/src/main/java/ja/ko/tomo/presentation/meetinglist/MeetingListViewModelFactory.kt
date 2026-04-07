package ja.ko.tomo.presentation.meetinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ja.ko.tomo.domain.usecase.meeting.GetMeetingsUseCase

class MeetingListViewModelFactory(
    private val getMeetingsUseCase: GetMeetingsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeetingListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeetingListViewModel(
                getMeetingsUseCase = getMeetingsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}