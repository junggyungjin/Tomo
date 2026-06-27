package ja.ko.tomo.domain.model

sealed interface FollowResult {
    data class Success(val isFollowing: Boolean): FollowResult
    data class Error(val message: String) : FollowResult
}