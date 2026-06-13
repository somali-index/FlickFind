package com.example.flickfind.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import com.example.flickfind.ui.theme.FlickFindTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onSavedMoviesClick: () -> Unit,
    onCollectionsClick: () -> Unit,
    onLogout: () -> Unit,
    onUnderDevelopmentClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    ProfileScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onSavedMoviesClick = onSavedMoviesClick,
        onCollectionsClick = onCollectionsClick,
        onLogout = onLogout,
        onUnderDevelopmentClick = onUnderDevelopmentClick,
        onShowChangeName = { viewModel.showChangeNameDialog(true) },
        onDismissChangeName = { viewModel.showChangeNameDialog(false) },
        onChangeName = { viewModel.updateName(it) },
        onShowChangeUsername = { viewModel.showChangeUsernameDialog(true) },
        onDismissChangeUsername = { viewModel.showChangeUsernameDialog(false) },
        onChangeUsername = { viewModel.updateUsername(it) },
        onShowChangePassword = { viewModel.showChangePasswordDialog(true) },
        onDismissChangePassword = { viewModel.showChangePasswordDialog(false) },
        onChangePassword = { current, new -> viewModel.changePassword(current, new) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    uiState: ProfileUiState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onSavedMoviesClick: () -> Unit,
    onCollectionsClick: () -> Unit,
    onLogout: () -> Unit,
    onUnderDevelopmentClick: () -> Unit,
    onShowChangeName: () -> Unit,
    onDismissChangeName: () -> Unit,
    onChangeName: (String) -> Unit,
    onShowChangeUsername: () -> Unit,
    onDismissChangeUsername: () -> Unit,
    onChangeUsername: (String) -> Unit,
    onShowChangePassword: () -> Unit,
    onDismissChangePassword: () -> Unit,
    onChangePassword: (String, String) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF0F0F0F),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "TÀI KHOẢN CỦA TÔI",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))

                // Avatar
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(120.dp)
                            .border(2.dp, Color(0xFF00E5FF), CircleShape)
                            .padding(6.dp)
                    ) {
                        AsyncImage(
                            model = uiState.avatar.ifEmpty { "https://picsum.photos/200" },
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = uiState.name.ifEmpty { "Người dùng" },
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onShowChangeName() }
                )
                Text(
                    text = uiState.username.ifEmpty { "@user" },
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onShowChangeUsername() }
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Thống kê Điện Ảnh",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Bookmark,
                        title = "Phim Đã Lưu",
                        count = uiState.quickSaveCount.toString(),
                        onClick = onSavedMoviesClick
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.PlayCircle,
                        title = "Phim Đã Xem",
                        count = uiState.watchedMoviesCount.toString(),
                        onClick = onUnderDevelopmentClick
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.LibraryMusic,
                        title = "Bộ Sưu Tập",
                        count = uiState.collectionsCount.toString(),
                        onClick = onCollectionsClick
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1A1A1A))
                ) {
                    MenuItem(icon = Icons.Default.Lock, title = "Đổi mật khẩu", onClick = onShowChangePassword)
                    MenuItem(icon = Icons.Default.Favorite, title = "Thể Loại Yêu Thích", onClick = onUnderDevelopmentClick)
                    MenuItem(icon = Icons.Default.History, title = "Lịch Sử Tìm Kiếm", onClick = onUnderDevelopmentClick)
                    MenuItem(icon = Icons.Default.CheckCircle, title = "Danh Sách Theo Dõi", onClick = onUnderDevelopmentClick)
                    MenuItem(icon = Icons.Default.Settings, title = "Cài Đặt Ứng Dụng", onClick = onUnderDevelopmentClick)
                    MenuItem(icon = Icons.Default.Notifications, title = "Thông Báo", onClick = onUnderDevelopmentClick)
                    MenuItem(icon = Icons.AutoMirrored.Filled.Chat, title = "Hỗ Trợ & Phản Hồi", showDivider = false, onClick = onUnderDevelopmentClick)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "ĐĂNG XUẤT",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Phiên bản: v2.4.1",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (uiState.isChangePasswordDialogVisible) {
        ChangePasswordDialog(
            onDismiss = onDismissChangePassword,
            onConfirm = onChangePassword,
            isLoading = uiState.isLoading
        )
    }

    if (uiState.isChangeNameDialogVisible) {
        EditFieldDialog(
            title = "Đổi tên hiển thị",
            label = "Tên hiển thị",
            initialValue = uiState.name,
            onDismiss = onDismissChangeName,
            onConfirm = onChangeName,
            isLoading = uiState.isLoading
        )
    }

    if (uiState.isChangeUsernameDialogVisible) {
        EditFieldDialog(
            title = "Đổi username",
            label = "Username",
            initialValue = uiState.username.removePrefix("@"),
            onDismiss = onDismissChangeUsername,
            onConfirm = onChangeUsername,
            isLoading = uiState.isLoading
        )
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF00E5FF))
        }
    }
}

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
    isLoading: Boolean
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Đổi mật khẩu", color = Color.White) },
        containerColor = Color(0xFF1A1A1A),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Mật khẩu hiện tại") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF00E5FF)
                    )
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Mật khẩu mới") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF00E5FF)
                    )
                )
                OutlinedTextField(
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    label = { Text("Xác nhận mật khẩu mới") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF00E5FF)
                    )
                )
                if (error != null) {
                    Text(text = error!!, color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newPassword != confirmNewPassword) {
                        error = "Mật khẩu mới không khớp"
                    } else if (newPassword.length < 6) {
                        error = "Mật khẩu phải có ít nhất 6 ký tự"
                    } else {
                        onConfirm(currentPassword, newPassword)
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.Black
                    )
                } else {
                    Text("Xác nhận", color = Color.Black)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color.Gray)
            }
        }
    )
}

@Composable
fun EditFieldDialog(
    title: String,
    label: String,
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    isLoading: Boolean
) {
    var value by remember { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, color = Color.White) },
        containerColor = Color(0xFF1A1A1A),
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF00E5FF)
                )
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(value) },
                enabled = !isLoading && value.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Color.Black)
                } else {
                    Text("Lưu", color = Color.Black)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color.Gray)
            }
        }
    )
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    count: String,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = Color(0xFF1A1A1A),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF00E5FF),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = Color.Gray,
                fontSize = 10.sp
            )
            Text(
                text = count,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    title: String,
    showDivider: Boolean = true,
    onClick: () -> Unit = {}
) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF00E5FF),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = Color.DarkGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FlickFindTheme {
        ProfileScreenContent(
            uiState = ProfileUiState(
                name = "GAY NGUYÊN",
                username = "@Gay",
                email = "abc@gmail.com",
                quickSaveCount = 142
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onBack = {},
            onSavedMoviesClick = {},
            onCollectionsClick = {},
            onLogout = {},
            onUnderDevelopmentClick = {},
            onShowChangeName = {},
            onDismissChangeName = {},
            onChangeName = {},
            onShowChangeUsername = {},
            onDismissChangeUsername = {},
            onChangeUsername = {},
            onShowChangePassword = {},
            onDismissChangePassword = {},
            onChangePassword = { _, _ -> }
        )
    }
}

@Preview
@Composable
fun ChangePasswordDialogPreview() {
    FlickFindTheme {
        ChangePasswordDialog(
            onDismiss = {},
            onConfirm = { _, _ -> },
            isLoading = false
        )
    }
}

@Preview
@Composable
fun EditFieldDialogPreview() {
    FlickFindTheme {
        EditFieldDialog(
            title = "Đổi tên hiển thị",
            label = "Tên hiển thị",
            initialValue = "GAY NGUYÊN",
            onDismiss = {},
            onConfirm = {},
            isLoading = false
        )
    }
}
