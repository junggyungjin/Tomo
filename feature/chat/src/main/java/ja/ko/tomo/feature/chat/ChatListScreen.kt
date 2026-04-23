package ja.ko.tomo.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ja.ko.tomo.core.ui.theme.DarkGray
import ja.ko.tomo.core.ui.theme.Gray
import ja.ko.tomo.core.ui.theme.TomoBlue
import ja.ko.tomo.domain.model.ChatRoom

@Composable
fun ChatListScreen(
    state: ChatListUiState,
    onChatRoomClick: (Long) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Gray) {
        when (state) {
            ChatListUiState.Loading -> {}
            ChatListUiState.Empty -> {}
            is ChatListUiState.Error -> {}
            is ChatListUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.chatRooms) { room ->
                        ChatRoomItem(room = room, onClick = { onChatRoomClick(room.id)})
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatRoomItem(room: ChatRoom, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지 대용 아이콘
            Icon(
                Icons.Default.Groups,
                contentDescription = null,
                tint = TomoBlue,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 제목 및 마지막 메시지
            Column(modifier = Modifier.weight(1f)) {
                Text(text = room.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = room.lastMessage, color = DarkGray, fontSize = 14.sp, maxLines = 1)
            }

            // 시간 및 안읽은 갯수
            Column(horizontalAlignment = Alignment.End) {
                Text(text = room.lastMessageTime, color = DarkGray, fontSize = 12.sp)
                if (room.unreadCount > 0) {
                    Box(
                        modifier = Modifier.size(20.dp).background(Color.Red, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = room.unreadCount.toString(), color = Color.White, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}