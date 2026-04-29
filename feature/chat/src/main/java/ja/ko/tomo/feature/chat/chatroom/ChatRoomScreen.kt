package ja.ko.tomo.feature.chat.chatroom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ja.ko.tomo.core.ui.theme.DarkGray
import ja.ko.tomo.core.ui.theme.Gray
import ja.ko.tomo.core.ui.theme.TomoBlue
import ja.ko.tomo.domain.model.ChatMessage
import ja.ko.tomo.feature.chat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    state: ChatRoomUiState,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.chat_room_title), fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = stringResource(R.string.chat_room_back_desc))
                    }
                }
            )
        },
        bottomBar = {
            if (state is ChatRoomUiState.Success) {
                ChatInputBar(
                    text = state.inputText,
                    onTextChange = onMessageChange,
                    onSendClick = onSendClick,
                    isSending = state.isSending
                )
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = Gray
        ) {
            when (state) {
                is ChatRoomUiState.Success -> {
                    if (state.messages.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = stringResource(R.string.chat_room_empty_messages), color = DarkGray)
                        }
                    }else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.messages) { message ->
                                ChatBubble(message = message)
                            }
                        }
                    }
                }
                ChatRoomUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = TomoBlue)
                    }
                }
                ChatRoomUiState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(R.string.chat_room_empty_messages), color = DarkGray)
                    }
                }
                is ChatRoomUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = Color.Red)
                    }
                }
            }
        }

    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isMine) Alignment.End else Alignment.Start
    val bubbleColor = if (message.isMine) TomoBlue else Color.White
    val textColor = if (message.isMine) Color.White else Color.Black

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        if (!message.isMine) {
            Text(
                text = message.senderNickname,
                fontSize = 12.sp,
                color = DarkGray,
                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
            )
        }
        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (message.isMine) 12.dp else 0.dp,
                bottomEnd = if (message.isMine) 0.dp else 12.dp
            )
        ) {
            Text(
                text = message.message,
                color = textColor,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                fontSize = 15.sp
            )
        }
        Text(
            text = message.sentTime,
            fontSize = 10.sp,
            color = DarkGray,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 8.dp, // 표면이 떠있게 보이게 함
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .navigationBarsPadding() // 시스템 영역만큼 하단에 여백을 주는 것
                .imePadding(), // 키보드 영역만큼 입력창을 위로 올려 줌
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text(stringResource(R.string.chat_room_input_placeholder)) },
                maxLines = 3,
                enabled = !isSending,
                shape = RoundedCornerShape(24.dp)
            )
            IconButton(
                onClick = onSendClick,
                enabled = !isSending && text.isNotBlank()
            ) {
                Icon(Icons.Default.Send, contentDescription = stringResource(R.string.chat_room_send_desc), tint = TomoBlue)
            }
        }
    }
}