package com.flickfind.app.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flickfind.app.ui.theme.FlickFindTheme
import com.flickfind.app.utils.DataStoreManager
import com.flickfind.app.utils.FirebaseAuthManager
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val dataStore = remember { DataStoreManager(context) }
    
    val email = FirebaseAuthManager.userEmail.ifBlank { "user@flickfind.com" }
    val displayName = FirebaseAuthManager.displayName

    var favCount by remember { mutableIntStateOf(0) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }

    val isDarkMode by dataStore.darkModeFlow.collectAsState(initial = false)

    LaunchedEffect(Unit) {
        favCount = dataStore.getFavoriteIds().size
    }

    ProfileContent(
        displayName = displayName,
        email = email,
        favCount = favCount,
        isDarkMode = isDarkMode,
        onDarkModeChange = { checked ->
            coroutineScope.launch {
                dataStore.setDarkMode(checked)
            }
        },
        onNavigateToAbout = onNavigateToAbout,
        onLogoutClick = { showLogoutDialog = true },
        onPasswordClick = { showPasswordDialog = true },
        onShowToast = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    )

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Đăng xuất") },
            text = { Text("Bạn có chắc muốn đăng xuất không?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    FirebaseAuthManager.signOut()
                    onLogout()
                }) { Text("Đăng xuất", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Hủy") }
            }
        )
    }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Đổi mật khẩu") },
            text = {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Mật khẩu mới") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newPassword.length < 6) {
                        Toast.makeText(context, "Mật khẩu phải từ 6 ký tự", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    showPasswordDialog = false
                    coroutineScope.launch {
                        FirebaseAuthManager.updatePassword(newPassword).onSuccess {
                            Toast.makeText(context, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                            newPassword = ""
                        }.onFailure {
                            Toast.makeText(context, it.message ?: "Lỗi", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) { Text("Lưu") }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) { Text("Hủy") }
            }
        )
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.headlineMedium, color = color, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun SettingsItem(icon: String, title: String, subtitle: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 20.sp, modifier = Modifier.padding(end = 16.dp))
        Text(text = title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        if (subtitle != null) {
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(end = 8.dp))
        }
        Text(text = "›", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ProfileContent(
    displayName: String,
    email: String,
    favCount: Int,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onNavigateToAbout: () -> Unit,
    onLogoutClick: () -> Unit,
    onPasswordClick: () -> Unit,
    onShowToast: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayName.firstOrNull()?.uppercase() ?: "?",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = displayName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(text = email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
        }

        // Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("Yêu thích", favCount.toString(), MaterialTheme.colorScheme.primary)
            StatItem("Đã xem", (favCount * 2).toString(), MaterialTheme.colorScheme.secondary)
        }

        Text(
            text = "CÀI ĐẶT",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column {
                SettingsItem(icon = "🔔", title = "Thông báo", onClick = { onShowToast("Tính năng đang phát triển") })
                Divider(modifier = Modifier.padding(start = 48.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🌙", fontSize = 20.sp, modifier = Modifier.padding(end = 16.dp))
                    Text(text = "Giao diện tối", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = onDarkModeChange
                    )
                }

                Divider(modifier = Modifier.padding(start = 48.dp))
                SettingsItem(icon = "🌐", title = "Ngôn ngữ", subtitle = "Tiếng Việt", onClick = { onShowToast("Tính năng đang phát triển") })
                Divider(modifier = Modifier.padding(start = 48.dp))
                SettingsItem(icon = "ℹ️", title = "Về ứng dụng", onClick = onNavigateToAbout)
                Divider(modifier = Modifier.padding(start = 48.dp))
                SettingsItem(icon = "🔒", title = "Đổi mật khẩu", onClick = onPasswordClick)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp)
        ) {
            Text("Đăng xuất", fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FlickFindTheme {
        ProfileContent(
            displayName = "Nguyễn Văn A",
            email = "vana@example.com",
            favCount = 12,
            isDarkMode = false,
            onDarkModeChange = {},
            onNavigateToAbout = {},
            onLogoutClick = {},
            onPasswordClick = {},
            onShowToast = {}
        )
    }
}
