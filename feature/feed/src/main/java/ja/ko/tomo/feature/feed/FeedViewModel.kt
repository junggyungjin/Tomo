package ja.ko.tomo.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.domain.feed.model.FeedResult
import ja.ko.tomo.domain.feed.model.RoomStatus
import ja.ko.tomo.domain.feed.usecase.GetFeedsUseCase
import ja.ko.tomo.domain.model.FeedFilter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedsUseCase: GetFeedsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeedUiState>(FeedUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<FeedUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        fetchFeeds()
    }

    /**
     * 피드 데이터를 가져오는 함수 (Intent)
     */
    fun fetchFeeds() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is FeedUiState.Success) {
                _uiState.update { currentState.copy(isRefreshing = true) }
            }else {
                _uiState.value = FeedUiState.Loading
            }

            when (val result = getFeedsUseCase()) {
                is FeedResult.Success -> {
                    _uiState.value = FeedUiState.Success(
                        feeds = result.feeds,
                        activeCallRooms = result.feeds.filter { it.callRoom != null && it.callRoom?.status == RoomStatus.OPEN},
                        isRefreshing = false
                    )
                }
                is FeedResult.Empty -> {
                    _uiState.value = FeedUiState.Success(
                        feeds = emptyList(),
                        activeCallRooms = emptyList(),
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
        }
    }

    /**
     * 사용자의 리프레시 액션 처리(Intent)
     */
    fun onRefresh() {
        fetchFeeds()
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

    fun onFilterClick(filter: FeedFilter) {
        val currentState = _uiState.value
        if (currentState is FeedUiState.Success) {
            _uiState.value = currentState.copy(selectedFilter = filter)
            //TODO 서버 개발 완료 시 여기서 filter에 따른 데이터 로드 호출
        }
    }

    /**
     * 피드 작성 버튼 클릭 시 처리 (Intent)
     */
    fun onCreateFeedClick() {
        viewModelScope.launch {
            _uiEffect.send(FeedUiEffect.NavigateToCreateFeed)
        }
    }
}