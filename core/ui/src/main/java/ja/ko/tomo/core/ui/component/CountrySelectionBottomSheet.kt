package ja.ko.tomo.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ja.ko.tomo.core.ui.theme.Gray
import ja.ko.tomo.core.ui.theme.White
import ja.ko.tomo.domain.model.Country
import ja.ko.tomo.domain.model.countries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountrySelectionBottomSheet(
    onDismissRequest: () -> Unit,
    onCountrySelect: (Country) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = White,
        dragHandle = { BottomSheetDefaults.DragHandle()}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "국적 선택",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn {
                items(countries) { country ->
                    ListItem(
                        headlineContent = { Text(country.name) },
                        leadingContent = { Text(country.flag, fontSize = 24.sp)},
                        trailingContent = { Text(country.code, color = Gray)},
                        modifier = Modifier.clickable{ onCountrySelect(country)}
                    )
                }
            }
        }
    }
}