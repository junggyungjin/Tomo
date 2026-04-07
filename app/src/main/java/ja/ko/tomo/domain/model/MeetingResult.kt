package ja.ko.tomo.domain.model

sealed interface MeetingResult {
    data class Success(val meeting: Meeting) : MeetingResult
    data object Empty : MeetingResult
    data class Error(val message: String) : MeetingResult
}