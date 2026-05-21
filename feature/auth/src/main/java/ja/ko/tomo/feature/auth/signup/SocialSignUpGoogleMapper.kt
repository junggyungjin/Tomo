package ja.ko.tomo.feature.auth.signup

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import ja.ko.tomo.core.ui.util.getJwtClaim

/**
 * GoogleIdTokenCredential에서 iOS(RN)와 동일한 기준의 고유 ID(sub)를 추출
 * Presentation Layer 내에서 외부 객체를 가공하기 위한 확장 프로퍼티
 */

val GoogleIdTokenCredential.uniqueId: String
    get() = idToken.getJwtClaim("sub") ?: id