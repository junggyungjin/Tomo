package ja.ko.tomo.feature.auth.onboading

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ja.ko.tomo.core.ui.component.CountrySelectionBottomSheet
import ja.ko.tomo.core.ui.component.TomoSnackbar
import ja.ko.tomo.core.ui.component.TomoStateView
import ja.ko.tomo.core.ui.theme.Black
import ja.ko.tomo.core.ui.theme.Gray
import ja.ko.tomo.core.ui.theme.LightGrey
import ja.ko.tomo.core.ui.theme.TomoBlue
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.core.ui.theme.White
import ja.ko.tomo.domain.model.Gender
import ja.ko.tomo.feature.auth.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    userId: String,
    state: ProfileSetupUiState,
    effect: Flow<ProfileSetupUiEffect>,
    onNicknameChange: (String) -> Unit,
    onGenderSelect: (Gender) -> Unit,
    onNationalitySelect: (String) -> Unit,
    onImageSelected: (Uri) -> Unit,
    onCompleteClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(effect) {
        effect.collect { uiEffect ->
            when (uiEffect) {
                is ProfileSetupUiEffect.NavigateToHome -> onNavigateToHome()
                is ProfileSetupUiEffect.NavigateBack -> onBackClick()
                is ProfileSetupUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = uiEffect.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                TomoSnackbar(snackbarData = data)
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.social_signup_title),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        TomoStateView(
            isLoading = state is ProfileSetupUiState.Loading,
            errorMessage = (state as? ProfileSetupUiState.Error)?.message,
            isSuccess = state is ProfileSetupUiState.Success,
            onRetry = onRetry,
            modifier = Modifier.padding(padding)
        ) {
            ProfileSetupContent(
                state = state as ProfileSetupUiState.Success,
                onNicknameChange = onNicknameChange,
                onGenderSelect = onGenderSelect,
                onNationalitySelect = onNationalitySelect,
                onImageSelected = onImageSelected,
                onCompleteClick = { onCompleteClick(userId) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileSetupContent(
    state: ProfileSetupUiState.Success,
    onNicknameChange: (String) -> Unit,
    onGenderSelect: (Gender) -> Unit,
    onNationalitySelect: (String) -> Unit,
    onImageSelected: (Uri) -> Unit,
    onCompleteClick: () -> Unit
) {
    // 바텀 시트 노출 여부 상태
    var showCountrySheet by remember { mutableStateOf(false) }

    // 갤러리 런처 설정
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onImageSelected(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.profile_setup_intro),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 30.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 프사 설정
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(LightGrey.copy(alpha = 0.5f))
                .border(1.dp, LightGrey.copy(alpha = 0.5f), CircleShape)
                .clickable { launcher.launch("image/*")},
            contentAlignment = Alignment.Center
        ) {
            if (state.selectedImageUri != null) {
                AsyncImage(
                    model = state.selectedImageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Gray
                )
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.BottomEnd)
                    .background(White, CircleShape)
                    .border(1.dp, LightGrey.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = TomoBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 닉네임 입력부
        OutlinedTextField(
            value = state.nickname,
            onValueChange = onNicknameChange,
            label = { Text(stringResource(R.string.profile_setup_nickname_label)) },
            placeholder = { Text(stringResource(R.string.profile_setup_nickname_placeholder)) },
            isError = state.nicknameError != null,
            supportingText = {
                state.nicknameError?.let { Text(it.asString()) }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 성별 선택부
        Text(text = "성별", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Gender.entries.filter { it != Gender.OTHER }.forEach { gender ->
                val isSelected = state.gender == gender
                val genderText = when (gender) {
                    Gender.MALE -> stringResource(R.string.profile_setup_gender_male)
                    Gender.FEMALE -> stringResource(R.string.profile_setup_gender_female)
                    else -> gender.name
                }
                FilterChip(
                    selected = isSelected,
                    onClick = { onGenderSelect(gender) },
                    label = { Text(genderText) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TomoBlue,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 국적 입력
        Text(text = stringResource(R.string.profile_setup_nationality_label),
            fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedCard(
            onClick = { showCountrySheet = true},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, if (state.nationality.isEmpty()) Color.LightGray else TomoBlue)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.nationality.isEmpty()) {
                        stringResource(R.string.profile_setup_nationality_placeholder)
                    }else {
                        state.nationality
                    },
                    color = if (state.nationality.isEmpty()) Gray else Black
                )
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
            }
        }

        // 바텀 시트 호출
        if (showCountrySheet) {
            CountrySelectionBottomSheet(
                onDismissRequest = { showCountrySheet = false },
                onCountrySelect = { country ->
                    onNationalitySelect(country.code)
                    showCountrySheet = false
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(40.dp))

        // 완료 버튼
        Button(
            onClick = onCompleteClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TomoBlue),
            shape = RoundedCornerShape(28.dp),
            enabled = !state.isSubmitting && state.nickname.isNotBlank()
        ) {
            if (state.isSubmitting) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = White)
            }else {
                Text(
                    text = stringResource(R.string.profile_setup_complete_button),
                    color = White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "프로필 설정 초기 화면")
@Composable
private fun ProfileSetupScreenPreview() {
    TomoTheme {
        ProfileSetupScreen(
            userId = "test_user",
            state = ProfileSetupUiState.Success(
                nickname = "",
                gender = null,
                nationality = ""
            ),
            effect = emptyFlow(),
            onNicknameChange = {},
            onGenderSelect = {},
            onNationalitySelect = {},
            onImageSelected = {},
            onCompleteClick = {},
            onBackClick = {},
            onNavigateToHome = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "프로필 설정 입력 중")
@Composable
private fun ProfileSetupScreenInputPreview() {
    TomoTheme {
        ProfileSetupScreen(
            userId = "test_user",
            state = ProfileSetupUiState.Success(
                nickname = "TomoUser",
                gender = Gender.MALE,
                nationality = "KR"
            ),
            effect = emptyFlow(),
            onNicknameChange = {},
            onGenderSelect = {},
            onNationalitySelect = {},
            onImageSelected = {},
            onCompleteClick = {},
            onBackClick = {},
            onNavigateToHome = {},
            onRetry = {}
        )
    }
}