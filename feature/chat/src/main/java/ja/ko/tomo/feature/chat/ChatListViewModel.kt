package ja.ko.tomo.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.ko.tomo.domain.model.ChatListResult
import ja.ko.tomo.domain.usecase.chat.GetChatRoomsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getChatRoomsUseCase: GetChatRoomsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChatListUiState>(ChatListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        observeChatRooms()
    }

    private fun observeChatRooms() {
        viewModelScope.launch {
            // Flow를 관찰하여 실시간으로 상태를 업데이트 하기 위함
            getChatRoomsUseCase().collect { result ->
                _uiState.value = when (result) {
                    is ChatListResult.Success -> ChatListUiState.Success(result.chatRooms)
                    ChatListResult.Empty -> ChatListUiState.Empty
                    is ChatListResult.Error -> ChatListUiState.Error(result.message)
                }
            }
        }
    }
}