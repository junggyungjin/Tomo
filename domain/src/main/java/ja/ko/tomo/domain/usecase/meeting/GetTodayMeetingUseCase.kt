package ja.ko.tomo.domain.usecase.meeting

import ja.ko.tomo.domain.model.MeetingResult
import ja.ko.tomo.domain.repository.MeetingRepository
import javax.inject.Inject

class GetTodayMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(): MeetingResult {
        return meetingRepository.getTodayMeeting()
    }
}