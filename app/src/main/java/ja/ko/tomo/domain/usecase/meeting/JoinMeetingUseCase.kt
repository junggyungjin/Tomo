package ja.ko.tomo.domain.usecase.meeting

import ja.ko.tomo.domain.model.MeetingResult
import ja.ko.tomo.domain.repository.MeetingRepository
import javax.inject.Inject

class JoinMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meetingId: Long): MeetingResult {
        return meetingRepository.joinMeeting(meetingId)
    }
}