package ja.ko.tomo.domain.usecase.meeting

import ja.ko.tomo.domain.model.MeetingListResult
import ja.ko.tomo.domain.repository.MeetingRepository
import javax.inject.Inject

class GetMeetingsUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(): MeetingListResult {
        return meetingRepository.getMeetings()
    }
}