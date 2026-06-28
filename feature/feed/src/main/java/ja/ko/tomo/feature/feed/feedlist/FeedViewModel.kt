package ja.ko.tomo.feature.feed.feedlist

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.core.ui.base.BaseViewModel
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.core.ui.util.toUiText
import ja.ko.tomo.domain.feed.model.FeedResult
import ja.ko.tomo.domain.feed.model.RoomStatus
import ja.ko.tomo.domain.feed.usecase.GetFeedsUseCase
import ja.ko.tomo.domain.feed.usecase.ToggleLikeUseCase
import ja.ko.tomo.domain.model.FeedFilter
import ja.ko.tomo.domain.model.FollowResult
import ja.ko.tomo.domain.usecase.user.ToggleFollowUseCase
import ja.ko.tomo.feature.feed.R
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedsUseCase: GetFeedsUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val toggleFollowUseCase: ToggleFollowUseCase
) : BaseViewModel<FeedUiState, FeedUiEffect>(FeedUiState.Loading) {

    init {
        fetchFeeds(FeedFilter.ALL)
    }

    /**
     * 피드 데이터를 가져오는 함수 (Intent)
     */
    fun fetchFeeds(filter: FeedFilter = FeedFilter.ALL) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                if (currentState is FeedUiState.Success) {
                    _uiState.update { currentState.copy(isRefreshing = true, selectedFilter = filter) }
                }else {
                    _uiState.value = FeedUiState.Loading
                }

                when (val result = getFeedsUseCase(filter)) {
                    is FeedResult.Success -> {
                        _uiState.value = FeedUiState.Success(
                            feeds = result.feeds,
                            activeCallRooms = result.feeds.filter { it.callRoom != null && it.callRoom?.status == RoomStatus.OPEN},
                            selectedFilter = filter,
                            isRefreshing = false
                        )
                    }
                    is FeedResult.Empty -> {
                        _uiState.value = FeedUiState.Success(
                            feeds = emptyList(),
                            activeCallRooms = emptyList(),
                            selectedFilter = filter,
                            isRefreshing = false
                        )
                    }
                    is FeedResult.Error -> {
                        val errorMsg = UiText.DynamicString(result.message)
                        _uiState.value = FeedUiState.Error(errorMsg)
                        _uiEffect.send(FeedUiEffect.ShowSnackBar(errorMsg))
                    }
                    else -> {

                    }
                }
            }catch (e: Exception) {
                handleError(e) { FeedUiState.Error(it) }
                _uiEffect.send(FeedUiEffect.ShowSnackBar(e.toUiText()))
            }
        }
    }

    /**
     * 사용자의 리프레시 액션 처리(Intent)
     */
    fun onRefresh() {
        val currentFilter = (_uiState.value as? FeedUiState.Success)?.selectedFilter ?: FeedFilter.ALL
        fetchFeeds(currentFilter)
    }

    /**
     * 피드 아이템 클릭 시 처리 (Intent)
     */
    fun onFeedClick(feedId: String) {
        viewModelScope.launch {
            _uiEffect.send(FeedUiEffect.NavigateToDetail(feedId))
        }
    }

    /**
     * 통화룸 스토리 클릭 시 처리 (Intent)
     */
    fun onCallRoomClick(callRoomId: String) {
        viewModelScope.launch {
            // TODO: 통화방 입장 로직 또는 상세 페이지 이동
            _uiEffect.send(FeedUiEffect.ShowSnackBar(
                UiText.StringResource(R.string.feed_enter_call_room, callRoomId)
            ))
        }
    }

    /**
     * 필터 탭 클릭 시 처리 (Intent)
     */
    fun onFilterClick(filter: FeedFilter) {
        val currentState = _uiState.value as? FeedUiState.Success ?: return

        // 같은 필터를 다시 클릭한 경우 무시
        if (currentState.selectedFilter == filter) return

        fetchFeeds(filter)
    }

    /**
     * 피드 작성 버튼 클릭 시 처리 (Intent)
     */
    fun onCreateFeedClick() {
        viewModelScope.launch {
            _uiEffect.send(FeedUiEffect.NavigateToCreateFeed)
        }
    }

    /**
     * 피드 좋아요 클릭 처리 (Intent)
     */
    fun onLikeClick(feedId: String) {
        viewModelScope.launch {
            val currentState = _uiState.value as? FeedUiState.Success ?: return@launch

            when (val result = toggleLikeUseCase(feedId)) {
                is FeedResult.LikeSuccess -> {
                    val updatedFeeds = currentState.feeds.map { feed ->
                        if (feed.id == result.feedId) {
                            feed.copy(
                                isLiked = result.isLiked,
                                likeCount = result.likeCount
                            )
                        } else {
                            feed
                        }
                    }
                    _uiState.update { currentState.copy(feeds = updatedFeeds) }
                }
                is FeedResult.Error -> {
                    _uiEffect.send(FeedUiEffect.ShowSnackBar(UiText.DynamicString(result.message)))
                }
                else -> {}
            }
        }
    }

    /**
     * 팔로우 클릭 처리 (Intent)
     */
    fun onFollowClick(userId: String) {
        val currentState = _uiState.value as? FeedUiState.Success ?: return
        if (currentState.isFollowSubmitting) return

        viewModelScope.launch {
            _uiState.update { currentState.copy(isFollowSubmitting = true) }

            when (val result = toggleFollowUseCase(userId)) {
                is FollowResult.Success -> {
                    // 3. 성공 시 리스트 내 해당 유저가 작성한 모든 피드의 팔로우 상태 업데이트
                    val updatedFeeds = currentState.feeds.map { feed ->
                        if (feed.authorId == userId) {
                            feed.copy(isAuthorFollowing = result.isFollowing)
                        } else {
                            feed
                        }
                    }
                    _uiState.update {
                        currentState.copy(
                            feeds = updatedFeeds,
                            isFollowSubmitting = false
                        )
                    }
                }
                is FollowResult.Error -> {
                    _uiState.update { currentState.copy(isFollowSubmitting = false) }
                    _uiEffect.send(FeedUiEffect.ShowSnackBar(UiText.DynamicString(result.message)))
                }
            }
        }
    }
}