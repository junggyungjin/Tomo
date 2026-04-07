package ja.ko.tomo.presentation.meeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ja.ko.tomo.domain.usecase.meeting.GetTodayMeetingUseCase
import ja.ko.tomo.domain.usecase.meeting.JoinMeetingUseCase

class TomoMeetingViewModelFactory(
    private val getTodayMeetingUseCase: GetTodayMeetingUseCase,
    private val joinMeetingUseCase: JoinMeetingUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TomoMeetingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TomoMeetingViewModel(
                getTodayMeetingUseCase = getTodayMeetingUseCase,
                joinMeetingUseCase = joinMeetingUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}