package ja.ko.tomo.domain.model

sealed interface MeetingDetailResult {
    data class Success(val meeting: Meeting) : MeetingDetailResult
    data class Error(val message: String) : MeetingDetailResult
}