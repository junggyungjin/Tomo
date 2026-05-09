package ja.ko.tomo.core.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import ja.ko.tomo.core.ui.R


/**
 * 앱 내에서 이메일 문의를 보내기 위한 확장 함수
 * @param to 수신자 메일 주소
 * @param subject 메일 제목
 * @param body 메일 본문 (기기 정보 등을 포함하면 CS처리에 용이)
 */
fun Context.sendInquiryEmail(
    to: String,
    subject: String? = null,
    body: String = ""
) {
    val prefix = getString(R.string.core_ui_inquiry_subject_tomo)

    val finalSubject = if (subject.isNullOrBlank()) {
        prefix
    } else {
        "$prefix $subject"
    }

    // 1. mailto: 스키마를 사용하여 이메일 앱만 필터링하도록 설정
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        putExtra(Intent.EXTRA_SUBJECT, finalSubject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        // 2. 유저가 여러 이메일 앱 중 선택할 수 있도록 Chooser를 띄움
        startActivity(Intent.createChooser(intent, getString(R.string.core_ui_inquiry_send)))
    }catch (e: Exception) {
        // 3. 기기에 이메일 앱이 하나도 없을 경우 예외 처리
        Toast.makeText(this, getString(R.string.core_ui_not_found_email), Toast.LENGTH_SHORT).show()

    }
}