package ja.ko.tomo.core.ui.util

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import ja.ko.tomo.core.ui.R

@Composable
fun DoubleBackToExitHandler(
    enabled: Boolean,
    exitMessage: UiText = UiText.StringResource(R.string.app_exit_message)
) {
    val context = LocalContext.current
    var backPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler(enabled = enabled) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < 2000) {
            (context as? Activity)?.finish()
        } else {
            backPressedTime = currentTime
            Toast.makeText(
                context,
                exitMessage.asString(context),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}