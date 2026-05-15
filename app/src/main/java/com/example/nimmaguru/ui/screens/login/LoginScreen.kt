package com.example.nimmaguru.ui.screens.login

import com.example.nimmaguru.setLocale
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.nimmaguru.R
import com.example.nimmaguru.ui.theme.RoyalBlue


@Composable
fun LoginScreen(onLoginSuccess: (Boolean) -> Unit) {
    val context = LocalContext.current
    val activity = context as Activity
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isMentor by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = RoyalBlue
        )
        Text(
            text = stringResource(R.string.login_subtitle),
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.padding(top = 16.dp)) {
            Button(
                onClick = { setLocale(activity, "kn") }
            ) {
                Text("ಕನ್ನಡ")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { setLocale(activity, "en") }
            ) {
                Text("English")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Toggle between Student/Mentor
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.student))
            Switch(
                checked = isMentor,
                onCheckedChange = { isMentor = it },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(stringResource(R.string.mentor))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onLoginSuccess(isMentor) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue)
        ) {
            Text(
                text = if (isMentor) stringResource(R.string.login_as_guru) 
                       else stringResource(R.string.login_as_student)
            )
        }
    }
}