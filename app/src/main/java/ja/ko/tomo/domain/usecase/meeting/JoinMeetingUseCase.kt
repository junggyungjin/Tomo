package ja.ko.tomo.domain.usecase.meeting

import ja.ko.tomo.domain.model.MeetingResult
import ja.ko.tomo.domain.repository.MeetingRepository

class JoinMeetingUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meetingId: Long): MeetingResult {
        return meetingRepository.joinMeeting(meetingId)
    }
}