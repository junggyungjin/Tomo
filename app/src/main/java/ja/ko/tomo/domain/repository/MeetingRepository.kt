package ja.ko.tomo.domain.repository

import ja.ko.tomo.domain.model.MeetingDetailResult
import ja.ko.tomo.domain.model.MeetingListResult
import ja.ko.tomo.domain.model.MeetingResult

interface MeetingRepository {
    suspend fun getTodayMeeting(): MeetingResult
    suspend fun joinMeeting(meetingId: Long): MeetingResult
    suspend fun cancelJoin(meetingId: Long): MeetingResult
    suspend fun getMeetings(): MeetingListResult
    suspend fun getMeetingDetail(meetingId: Long): MeetingDetailResult
}