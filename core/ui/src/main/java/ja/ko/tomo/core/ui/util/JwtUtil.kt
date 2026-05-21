package ja.ko.tomo.core.ui.util

import android.util.Base64
import org.json.JSONObject
import java.nio.charset.Charset

/**
 * String(JWT)에서 특정 클레임을 추출하는 확장 함수
 */
fun String.getJwtClaim(claimName: String): String? {
    return try {
        val parts = this.split(".")
        if (parts.size < 2) return null

        val payload = String(
            Base64.decode(parts[1], Base64.URL_SAFE),
            Charset.forName("UTF-8")
        )

        val jsonObject = JSONObject(payload)
        jsonObject.optString(claimName).takeIf { it.isNotEmpty() }
    }catch (e: Exception) {
        null
    }
}