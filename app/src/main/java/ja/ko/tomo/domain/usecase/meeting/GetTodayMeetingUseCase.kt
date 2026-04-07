package ja.ko.tomo.domain.usecase.meeting

import ja.ko.tomo.domain.model.MeetingResult
import ja.ko.tomo.domain.repository.MeetingRepository

class GetTodayMeetingUseCase(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(): MeetingResult {
        return meetingRepository.getTodayMeeting()
    }
}