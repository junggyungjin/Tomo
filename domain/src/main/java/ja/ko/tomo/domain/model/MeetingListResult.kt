package ja.ko.tomo.domain.model

sealed interface MeetingListResult {
    data class Success(val meetings: List<Meeting>) : MeetingListResult
    data object Empty : MeetingListResult
    data class Error(val message: String) : MeetingListResult
}