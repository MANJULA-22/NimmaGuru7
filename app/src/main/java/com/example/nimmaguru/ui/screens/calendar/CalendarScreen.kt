package com.example.nimmaguru.ui.screens.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nimmaguru.R
import com.example.nimmaguru.ui.components.MentorshipClassCard
import com.example.nimmaguru.ui.theme.RoyalBlue
import com.example.nimmaguru.viewmodel.CalendarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel()
) {
    val classes by viewModel.classes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.class_calendar_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RoyalBlue,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = RoyalBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.upcoming_sessions),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (classes.isEmpty()) {
                        item {
                            Text(stringResource(R.string.no_classes_found))
                        }
                    } else {
                        items(classes) { mentorshipClass ->
                            MentorshipClassCard(mentorshipClass = mentorshipClass)
                        }
                    }
                }
            }
        }
    }
}