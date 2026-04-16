package ja.ko.tomo.domain.usecase.meeting

import ja.ko.tomo.domain.model.MeetingDetailResult
import ja.ko.tomo.domain.repository.MeetingRepository
import javax.inject.Inject

class GetMeetingDetailUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meetingId: Long) : MeetingDetailResult {
        return meetingRepository.getMeetingDetail(meetingId = meetingId)
    }
}