package com.example.nimmaguru.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nimmaguru.R
import com.example.nimmaguru.data.model.Guru
import com.example.nimmaguru.ui.components.SkillChipGroup
import com.example.nimmaguru.ui.theme.RoyalBlue
import com.example.nimmaguru.viewmodel.GuruProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuruProfileScreen(
    onProfileSaved: () -> Unit,
    viewModel: GuruProfileViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var availability by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val selectedSkills = remember { mutableStateListOf<String>() }
    
    val allSkills = stringArrayResource(R.array.skills_array).toList()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.edit_profile),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ) 
                },
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Full Name
            Text(
                stringResource(R.string.full_name),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text(stringResource(R.string.elderly_tip_name)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
            )
            
            // Village
            Text(
                stringResource(R.string.village),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = village,
                onValueChange = { village = it },
                placeholder = { Text(stringResource(R.string.elderly_tip_village)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
            )

            // Skills Selection
            Text(
                stringResource(R.string.select_skills),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            SkillChipGroup(
                allSkills = allSkills,
                selectedSkills = selectedSkills,
                onSkillSelected = { skill ->
                    if (selectedSkills.contains(skill)) selectedSkills.remove(skill)
                    else selectedSkills.add(skill)
                }
            )

            // Availability
            Text(
                stringResource(R.string.availability_label),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = availability,
                onValueChange = { availability = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
            )

            // About
            Text(
                stringResource(R.string.about_you),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text(stringResource(R.string.elderly_tip_about)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val guru = Guru(
                        name = name,
                        village = village,
                        skills = selectedSkills.toList(),
                        availability = availability,
                        description = description
                    )
                    viewModel.saveProfile(guru, null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp), // Larger touch target
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                enabled = uiState !is GuruProfileViewModel.ProfileUiState.Loading
            ) {
                if (uiState is GuruProfileViewModel.ProfileUiState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(
                        stringResource(R.string.save_profile),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (uiState is GuruProfileViewModel.ProfileUiState.Error) {
                Text(
                    text = (uiState as GuruProfileViewModel.ProfileUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            LaunchedEffect(uiState) {
                if (uiState is GuruProfileViewModel.ProfileUiState.Success) {
                    onProfileSaved()
                }
            }
        }
    }
}

@Composable
fun stringArrayResource(id: Int): Array<String> {
    return androidx.compose.ui.res.stringArrayResource(id)
}