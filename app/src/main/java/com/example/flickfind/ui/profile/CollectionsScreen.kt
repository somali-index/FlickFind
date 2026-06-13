package com.example.flickfind.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flickfind.DATALAYER.DataClass.DataCollection
import com.example.flickfind.ui.common.LoadingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(
    onBack: () -> Unit,
    onCollectionClick: (String, String) -> Unit,
    viewModel: CollectionsViewModel = viewModel()
) {
    val collections by viewModel.collections.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()
    val isSlowLoading by viewModel.isSlowLoading.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingCollection by remember { mutableStateOf<DataCollection?>(null) }
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Chưa có bộ sưu tập nào")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(collections) { collection ->
                    ListItem(
                        headlineContent = { Text(collection.CollectionName) },
                        leadingContent = { Icon(Icons.Default.Folder, contentDescription = null) },
                        trailingContent = {
                            androidx.compose.foundation.layout.Row {
                                IconButton(onClick = {
                                    editingCollection = collection
                                    newCollectionName = collection.CollectionName
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Sửa")
                                }
                                IconButton(onClick = {
                                    viewModel.deleteCollection(collection.IDCollection, collection.CollectionName)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Xóa")
                                }
                            }
                        },
                        modifier = Modifier.clickable {
                            onCollectionClick(collection.IDCollection, collection.CollectionName)
                        }
                    )
                    HorizontalDivider()
                }
            }
        }

        LoadingDialog(
            visible = isSlowLoading,
            message = "Đang tạo bộ sưu tập..."
        )
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

    editingCollection?.let { collection ->
        AlertDialog(
            onDismissRequest = { editingCollection = null },
            title = { Text("Sửa bộ sưu tập") },
            text = {
                TextField(
                    value = newCollectionName,
                    onValueChange = { newCollectionName = it },
                    placeholder = { Text("Nhập tên mới...") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateCollection(collection.IDCollection, newCollectionName)
                    editingCollection = null
                    newCollectionName = ""
                }) {
                    Text("Lưu")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingCollection = null }) {
                    Text("Hủy")
                }
            }
        )
    }
}
