package ja.ko.tomo.feature.auth.signup

sealed interface SocialSignUpUiState {
    data object Loading : SocialSignUpUiState
    data class Error(val message: String) : SocialSignUpUiState
    data class Success(
        val isSigngUp: Boolean = false, // 가입 버튼 클릭 후 서버 통신 중일 때 (MeetingCreate의 isSubmitting과 동일 역할)
        val isSocialLoding: Boolean = false // 특히 소셜 로그인이 진행 중인지 별도 관리 가능
    ) : SocialSignUpUiState
}