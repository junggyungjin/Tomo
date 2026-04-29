package ja.ko.tomo.feature.chat.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.feature.chat.chatlist.ChatListScreen
import ja.ko.tomo.feature.chat.chatlist.ChatListViewModel
import ja.ko.tomo.feature.chat.chatroom.ChatRoomScreen
import ja.ko.tomo.feature.chat.chatroom.ChatRoomViewModel

fun NavGraphBuilder.chatGraph(
    navController: NavController
) {
    composable(TomoNavRoutes.ChatList) {
        val chatViewModel: ChatListViewModel = hiltViewModel()
        val chatListUiState by chatViewModel.uiState.collectAsStateWithLifecycle()

        ChatListScreen(
            state = chatListUiState,
            onChatRoomClick = { chatId ->
                navController.navigate(
                    TomoNavRoutes.chatRoomRoute(chatId)
                )
            }
        )
    }

    composable(
        route = "${TomoNavRoutes.ChatRoom}/{chatId}",
        arguments = listOf(navArgument("chatId") { type = NavType.LongType })
    ) {
        val chatRoomViewModel: ChatRoomViewModel = hiltViewModel()
        val uiState by chatRoomViewModel.uiState.collectAsStateWithLifecycle()

        ChatRoomScreen(
            state = uiState,
            onMessageChange = chatRoomViewModel::onMessageChange,
            onSendClick = chatRoomViewModel::sendMessage,
            onBackClick = { navController.popBackStack() }
        )
    }
}