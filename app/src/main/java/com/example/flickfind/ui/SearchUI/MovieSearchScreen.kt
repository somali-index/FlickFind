package com.example.flickfind.ui.SearchUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieSearchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1. Ô NHẬP TÌM KIẾM PHIM
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { chuMoiGoc ->
                viewModel.onSearchQueryChange(chuMoiGoc)
            },
            label = { Text("Nhập tên phim cần tìm (ví dụ: Conan, Naruto)...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. DANH SÁCH CÁC BỘ PHIM TÌM ĐƯỢC
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(uiState.movieList) { phim ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = phim.NameMovie, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Thời lượng: ${phim.TimeOneEP}", style = MaterialTheme.typography.bodySmall)
                        Text(text = phim.NummberEP, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }

        // 3. THÔNG BÁO NẾU KHÔNG TÌM THẤY PHIM
        if (uiState.movieList.isEmpty() && uiState.searchQuery.isNotBlank()) {
            Text(
                text = "Không tìm thấy bộ phim nào phù hợp mục tiêu @@",
                modifier = Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}