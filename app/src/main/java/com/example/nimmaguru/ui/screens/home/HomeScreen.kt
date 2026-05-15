package com.example.nimmaguru.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nimmaguru.R
import com.example.nimmaguru.ui.components.GuruCard
import com.example.nimmaguru.ui.theme.RoyalBlue
import com.example.nimmaguru.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGuruClick: (String, String) -> Unit,
    onWallOfFameClick: () -> Unit,
    onCalendarClick: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val gurus by viewModel.gurus.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val allSkills = stringArrayResource(R.array.skills_array).toList()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    actions = {
                        IconButton(onClick = onCalendarClick) {
                            Icon(Icons.Default.DateRange, contentDescription = "Class Calendar")
                        }
                        IconButton(onClick = onWallOfFameClick) {
                            Icon(Icons.Default.Star, contentDescription = "Wall of Fame")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = RoyalBlue,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text(stringResource(R.string.search_placeholder)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true
                )

                // Skill Filter Row
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(allSkills) { skill ->
                        FilterChip(
                            selected = selectedSubject == skill,
                            onClick = { viewModel.toggleSkill(skill) },
                            label = { Text(skill) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = RoyalBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (gurus.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.no_gurus_found),
                            modifier = Modifier.padding(top = 32.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    items(gurus) { guru ->
                        GuruCard(guru = guru, onClick = { onGuruClick(guru.id, guru.name) })
                    }
                }
            }
        }
    }
}