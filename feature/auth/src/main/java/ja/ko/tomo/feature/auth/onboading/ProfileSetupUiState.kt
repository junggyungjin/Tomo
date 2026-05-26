package ja.ko.tomo.feature.auth.onboading

import android.net.Uri
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.domain.model.Gender

sealed interface ProfileSetupUiState {
    data object Loading : ProfileSetupUiState
    data class Error(val message: UiText): ProfileSetupUiState
    data class Success(
        val nickname: String = "",
        val nationality: String = "",
        val gender: Gender? = null,
        val profileImageUrl: String? = null,
        val selectedImageUri: Uri? = null, // 갤러리에서 선택한 로컬 이미지 URI
        val isSubmitting: Boolean = false, // 저장 버튼 클릭 시 로딩 선택
        val nicknameError: UiText? = null // 닉네임 유효성 에러 메시지
    ) : ProfileSetupUiState
}