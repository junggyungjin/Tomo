package ja.ko.tomo.domain.usecase.meeting

import ja.ko.tomo.domain.model.MeetingResult
import ja.ko.tomo.domain.repository.MeetingRepository
import javax.inject.Inject

class CreateMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(
        title: String,
        subtitle: String,
        dateTime: String,
        location: String,
        capacity: Int
    ): MeetingResult {
        if (title.isBlank()) return MeetingResult.Error("제목을 입력해주세요")

        return meetingRepository.createMeeting(
            title = title,
            subtitle = subtitle,
            dateTime = dateTime,
            location = location,
            capacity = capacity
        )
    }
}