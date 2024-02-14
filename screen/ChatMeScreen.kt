package com.example.chatroomapp.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ChatMeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    exitConfirmationListener: ExitConfirmationListener
) {

    var showDialog by remember { mutableStateOf(false) }

    // Handle back button press
    BackHandler(onBack = {
        showDialog = true
    })


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textColor = MaterialTheme.colorScheme.onBackground
            Text(
                text = "CHAT BOT",
                fontWeight = FontWeight.Bold,
                color = textColor,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onNavigateToSignUp() },
                modifier = Modifier.width(150.dp)
            ) {
                Text("Sign Up")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onNavigateToLogin() },
                modifier = Modifier.width(150.dp)
            ) {
                Text("Log In")
            }
        }
        if (showDialog) {
            ExitConfirmationDialog(
                onConfirmExit = {
                    exitConfirmationListener.onConfirmExit()
                },
                onDismiss = {
                    showDialog = false
                }
            )
        }

    }
}

@Composable
fun ExitConfirmationDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Exit?") },
        text = { Text("Are you sure you want to exit?") },
        confirmButton = {
            Button(
                onClick = { onConfirmExit() }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text("No")
            }
        }
    )
}

