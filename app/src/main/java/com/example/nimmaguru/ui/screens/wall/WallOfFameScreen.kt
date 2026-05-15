package com.example.nimmaguru.ui.screens.wall

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nimmaguru.R
import com.example.nimmaguru.ui.components.GuruCard
import com.example.nimmaguru.ui.theme.RoyalBlue
import com.example.nimmaguru.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallOfFameScreen(
    onGuruClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val wallGurus by viewModel.wallOfFameGurus.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val subjects = listOf(stringResource(R.string.all_subjects)) + stringArrayResource(R.array.skills_array).toList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.wall_of_fame_title), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RoyalBlue,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Fast Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = MaterialTheme.shapes.medium
            )

            // Subject Filter
            Text(
                text = stringResource(R.string.filter_by_subject),
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subjects) { subject ->
                    FilterChip(
                        selected = (selectedSubject == subject) || (selectedSubject == null && subject == subjects[0]),
                        onClick = { viewModel.onSubjectSelected(subject) },
                        label = { Text(subject) }
                    )
                }
            }

            Text(
                text = stringResource(R.string.top_heroes),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (wallGurus.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                stringResource(R.string.no_wall_of_fame),
                                modifier = Modifier.padding(24.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                } else {
                    items(wallGurus) { guru ->
                        GuruCard(guru = guru, onClick = { onGuruClick(guru.id) })
                    }
                }
            }
        }
    }
}