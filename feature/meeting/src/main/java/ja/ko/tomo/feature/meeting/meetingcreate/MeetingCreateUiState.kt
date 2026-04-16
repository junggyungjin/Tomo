package ja.ko.tomo.feature.meeting.meetingcreate

sealed interface MeetingCreateUiState {
    data object Loading : MeetingCreateUiState
    data class Success(
        val title: String = "",
        val subtitle: String = "",
        val dateTime: String = "",
        val location: String = "",
        val capacity: String = "",
        val isSaveEnabled: Boolean = false,
        val isSubmitting: Boolean = false // 생성 버튼 클릭 후 서버 통신 중일 때 표시용
    ) : MeetingCreateUiState

    data class Error(val message: String) : MeetingCreateUiState

}