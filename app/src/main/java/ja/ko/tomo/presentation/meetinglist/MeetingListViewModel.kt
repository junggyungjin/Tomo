package ja.ko.tomo.presentation.meetinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.domain.model.MeetingListResult
import ja.ko.tomo.domain.usecase.meeting.GetMeetingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingListViewModel @Inject constructor(
    private val getMeetingsUseCase: GetMeetingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MeetingListUiState>(
        MeetingListUiState.Loading
    )
    val uiState : StateFlow<MeetingListUiState> = _uiState

    private var currentFilter: MeetingListFilter = MeetingListFilter.ALL

    init {
        loadMeetings()
    }

    fun reloadMeetings() {
        loadMeetings()
    }

    private fun loadMeetings() {
        _uiState.value = MeetingListUiState.Loading

        viewModelScope.launch {
            when (val result = getMeetingsUseCase()) {
                is MeetingListResult.Success -> {
                    val filteredMeetings = when (currentFilter) {
                        MeetingListFilter.ALL -> result.meetings
                        MeetingListFilter.JOINED -> result.meetings.filter { it.isJoined }
                    }

                    _uiState.value = MeetingListUiState.Success(
                        meetings = filteredMeetings,
                        selectedFilter = currentFilter
                    )
                }

                MeetingListResult.Empty -> {
                    _uiState.value = MeetingListUiState.Empty
                }

                is MeetingListResult.Error -> {
                    _uiState.value = MeetingListUiState.Error(result.message)
                }
            }
        }
    }

    fun updateFilter(filter: MeetingListFilter) {
        currentFilter = filter
        loadMeetings()
    }
}