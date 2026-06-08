package ja.ko.tomo.data.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * 서버의 ISO 8601 날짜 문자열을 Date 객체로 변환하는 유틸리티 (ADDED)
 * 데이터 레이어 내부에서 매핑 용도로만 사용됨
 */
fun String.toApiDate(): Date {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        this.let { format.parse(it) } ?: Date()
    } catch (e: Exception) {
        Date()
    }
}