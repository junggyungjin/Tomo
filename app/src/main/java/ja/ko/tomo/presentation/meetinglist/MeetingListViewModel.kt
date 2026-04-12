package ja.ko.tomo.presentation.meetinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.domain.model.MeetingListResult
import ja.ko.tomo.domain.model.MeetingResult
import ja.ko.tomo.domain.usecase.meeting.GetMeetingsUseCase
import ja.ko.tomo.domain.usecase.meeting.ToggleFavoriteUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingListViewModel @Inject constructor(
    private val getMeetingsUseCase: GetMeetingsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MeetingListUiState>(
        MeetingListUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private var currentFilter: MeetingListFilter = MeetingListFilter.ALL
    private var currentSearchQuery: String = ""
    private var searchJob: Job? = null

    init {
        loadMeetings()
    }

    fun reloadMeetings() {
        loadMeetings()
    }

    private fun loadMeetings(isSilent: Boolean = false) {
        if (!isSilent) {
            _uiState.value = MeetingListUiState.Loading
        }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            when (val result = getMeetingsUseCase(currentSearchQuery)) {
                is MeetingListResult.Success -> {
                    val filteredMeetings = when (currentFilter) {
                        MeetingListFilter.ALL -> result.meetings
                        MeetingListFilter.JOINED -> result.meetings.filter { it.isJoined }
                    }

                    _uiState.value = MeetingListUiState.Success(
                        meetings = filteredMeetings,
                        selectedFilter = currentFilter,
                        searchQuery = currentSearchQuery
                    )
                }

                MeetingListResult.Empty -> {
                    _uiState.value = MeetingListUiState.Success(
                        meetings = emptyList(),
                        selectedFilter = currentFilter,
                        searchQuery = currentSearchQuery
                    )
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

    fun onSearchQueryChange(newQuery: String) {
        currentSearchQuery = newQuery

        val currentState = _uiState.value
        if (currentState is MeetingListUiState.Success) {
            _uiState.value = currentState.copy(searchQuery = newQuery)
        }

        loadMeetings(isSilent = true)
    }

    fun toggleFavorite(meetingId: Long) {
        viewModelScope.launch {
            val result = toggleFavoriteUseCase(meetingId = meetingId)
            if (result is MeetingResult.Success) {
                loadMeetings(isSilent = true)
            }
        }
    }
}