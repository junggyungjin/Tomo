package ja.ko.tomo.data.repository

import ja.ko.tomo.domain.model.Meeting
import ja.ko.tomo.domain.model.MeetingDetailResult
import ja.ko.tomo.domain.model.MeetingListResult
import ja.ko.tomo.domain.model.MeetingResult
import ja.ko.tomo.domain.repository.MeetingRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeMeetingRepositoryImpl @Inject constructor(): MeetingRepository {

    private var meeting: Meeting? = Meeting(
        id = 1L,
        title = "서울 한일 언어교환 모임",
        subtitle = "한국어 + 일본어 자유대화",
        dateTime = "2026.03.22 일요일 19:00",
        location = "홍대입구 카페 하루",
        isClosed = false,
        isJoined = false,
        isFavorite = false,
        capacity = 4
    )

    private var meetings: List<Meeting> = listOf(
        Meeting(
            id = 1L,
            title = "서울 한일 언어교환 모임",
            subtitle = "한국어 + 일본어 자유대화",
            dateTime = "2026.03.22 일요일 19:00",
            location = "홍대입구 카페 하루",
            isClosed = false,
            isJoined = false,
            isFavorite = false,
            capacity = 4
        ),
        Meeting(
            id = 2L,
            title = "강남 일본어 스터디",
            subtitle = "JLPT N2 수준 회화 스터디",
            dateTime = "2026.03.23 월요일 18:30",
            location = "강남역 스터디룸",
            isClosed = false,
            isJoined = false,
            isFavorite = false,
            capacity = 4
        ),
        Meeting(
            id = 3L,
            title = "성수 한일 교류 번개",
            subtitle = "가볍게 커피 마시며 자유대화",
            dateTime = "2026.03.24 화요일 20:00",
            location = "성수동 카페",
            isClosed = false,
            isJoined = false,
            isFavorite = false,
            capacity = 4
        ),
        Meeting(
            id = 4L,
            title = "19금",
            subtitle = "메쨔쿠쨔 세크수",
            dateTime = "2026.03.29 토요일 22:00",
            location = "우리집",
            isClosed = false,
            isJoined = false,
            isFavorite = false,
            capacity = 2
        ),
        Meeting(
            id = 5L,
            title = "합정 한일 교류 번개",
            subtitle = "정신이 망신 술 파티",
            dateTime = "2026.03.28 금요일 20:00",
            location = "합정역 술집",
            isClosed = false,
            isJoined = false,
            isFavorite = false,
            capacity = 8
        )
    )

    override suspend fun getTodayMeeting(): MeetingResult {
        delay(1000)
        return meeting?.let {
            MeetingResult.Success(it)
        } ?: MeetingResult.Empty
    }

    override suspend fun joinMeeting(meetingId: Long): MeetingResult {
        val targetMeeting = meetings.find { it.id == meetingId }
            ?: return MeetingResult.Error("존재하지 않는 모임입니다")

        if (targetMeeting.isClosed) {
            return MeetingResult.Error("이미 마감된 모임입니다")
        }

        if (targetMeeting.isJoined) {
            return MeetingResult.Error("이미 참가한 모임입니다")
        }

        val updatedMeeting = targetMeeting.copy(
            isJoined = true
        )

        meetings = meetings.map {
            if (it.id == meetingId) updatedMeeting else it
        }

        if (meeting?.id == meetingId) {
            meeting = updatedMeeting
        }

        return MeetingResult.Success(updatedMeeting)
    }

    override suspend fun cancelJoin(meetingId: Long): MeetingResult {
        val targetMeeting = meetings.find { it.id == meetingId }
            ?: return MeetingResult.Error("존재하지 않는 모임입니다")

        if (!targetMeeting.isJoined) {
            return MeetingResult.Error("참가하지 않은 모임입니다")
        }

        val updatedMeeting = targetMeeting.copy(
            isJoined = false
        )

        meetings = meetings.map {
            if (it.id == meetingId) updatedMeeting else it
        }

        if (meeting?.id == meetingId) {
            meeting = updatedMeeting
        }

        return MeetingResult.Success(updatedMeeting)
    }

    override suspend fun toggleFavorite(meetingId: Long): MeetingResult {
        val targetMeeting = meetings.find { it.id == meetingId }
            ?: return MeetingResult.Error("존재하지 않는 모임입니다")

        val updatedMeeting = targetMeeting.copy(isFavorite = !targetMeeting.isFavorite)

        meetings = meetings.map { if (it.id == meetingId) updatedMeeting else it}

        if (meeting?.id == meetingId) {
            meeting = updatedMeeting
        }

        return MeetingResult.Success(updatedMeeting)
    }

    override suspend fun createMeeting(
        title: String,
        subtitle: String,
        dateTime: String,
        location: String,
        capacity: Int
    ): MeetingResult {
        delay(1000)

        val newMeeting = Meeting(
            id = (meetings.size + 1).toLong(),
            title = title,
            subtitle = subtitle,
            dateTime = dateTime,
            location = location,
            isClosed = false,
            isJoined = false,
            isFavorite = false,
            capacity = capacity
        )

        meetings = meetings + newMeeting
        return MeetingResult.Success(newMeeting)
    }

    override suspend fun getMeetings(query: String?): MeetingListResult {
        delay(1000)

        val filtered = if (query.isNullOrBlank()) {
            meetings
        }else {
            meetings.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.location.contains(query, ignoreCase = true)
            }
        }

        return if (filtered.isEmpty()) {
            MeetingListResult.Empty
        }else {
            MeetingListResult.Success(filtered)
        }
    }

    override suspend fun getMeetingDetail(meetingId: Long): MeetingDetailResult {
        delay(300)

        val meeting = meetings.find { it.id == meetingId }

        return if (meeting != null) {
            MeetingDetailResult.Success(meeting)
        } else {
            MeetingDetailResult.Error("존재하지 않는 모임입니다.")
        }
    }
}