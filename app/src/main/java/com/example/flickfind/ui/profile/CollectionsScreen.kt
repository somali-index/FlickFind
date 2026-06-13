package com.example.flickfind.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(
    onBack: () -> Unit,
    onCollectionClick: (String, String) -> Unit,
    viewModel: CollectionsViewModel = viewModel()
) {
    val collections by viewModel.collections.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newCollectionName by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bộ sưu tập của tôi") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Thêm bộ sưu tập")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (collections.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Chưa có bộ sưu tập nào")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(collections) { collection ->
                    ListItem(
                        headlineContent = { Text(collection.CollectionName) },
                        leadingContent = { Icon(Icons.Default.Folder, contentDescription = null) },
                        modifier = Modifier.clickable {
                            onCollectionClick(collection.IDCollection, collection.CollectionName)
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Tạo bộ sưu tập mới") },
            text = {
                TextField(
                    value = newCollectionName,
                    onValueChange = { newCollectionName = it },
                    placeholder = { Text("Nhập tên bộ sưu tập...") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.createCollection(newCollectionName)
                    newCollectionName = ""
                    showAddDialog = false
                }) {
                    Text("Tạo")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
