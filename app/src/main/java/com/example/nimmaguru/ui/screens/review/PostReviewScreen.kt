package com.example.nimmaguru.ui.screens.review

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nimmaguru.R
import com.example.nimmaguru.ui.theme.RoyalBlue
import com.example.nimmaguru.viewmodel.ReviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostReviewScreen(
    guruId: String,
    guruName: String,
    onBack: () -> Unit,
    viewModel: ReviewViewModel = viewModel()
) {
    var studentName by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5) }
    val isPosting by viewModel.isPosting.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.thank_you_title, guruName)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RoyalBlue,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = studentName,
                onValueChange = { studentName = it },
                label = { Text(stringResource(R.string.your_name)) },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(stringResource(R.string.your_rating))
            Slider(
                value = rating.toFloat(),
                onValueChange = { rating = it.toInt() },
                valueRange = 1f..5f,
                steps = 3
            )
            Text(
                text = stringResource(R.string.rating_display, rating),
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text(stringResource(R.string.write_thank_you)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    viewModel.postReview(guruId, studentName, note, rating, onBack)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                enabled = !isPosting && note.isNotEmpty() && studentName.isNotEmpty()
            ) {
                if (isPosting) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(stringResource(R.string.post_appreciation))
                }
            }
        }
    }
}