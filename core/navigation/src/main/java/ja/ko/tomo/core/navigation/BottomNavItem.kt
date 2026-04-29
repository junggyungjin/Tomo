package ja.ko.tomo.core.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    @StringRes val nameRes: Int,
    val route: String,
    val icon: ImageVector
)

val bottomNavItem = listOf(
    BottomNavItem(R.string.nav_meeting, TomoNavRoutes.MeetingList, Icons.Default.Groups),
    BottomNavItem(R.string.nav_chat, TomoNavRoutes.ChatList, Icons.Default.ChatBubble),
    BottomNavItem(R.string.nav_mypage, TomoNavRoutes.MyPage, Icons.Default.Person)
)