package ja.ko.tomo.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItem = listOf(
    BottomNavItem("모임", TomoNavRoutes.MeetingList, Icons.Default.Groups),
    BottomNavItem("채팅", TomoNavRoutes.ChatList, Icons.Default.ChatBubble),
    BottomNavItem("내 정보", TomoNavRoutes.MyPage, Icons.Default.Person)
)