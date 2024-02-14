package com.example.chatroomapp.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.chatroomapp.data.result
import com.example.chatroomapp.viewmodel.AuthViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onSignInSuccess:()->Unit,
    exitConfirmationListener: ExitConfirmationListener
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val result by authViewModel.authResult.observeAsState()
    val isLoading by authViewModel.isLoading.observeAsState()
    var isButtonClicked by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    // Handle back button press
    BackHandler(onBack = {
        showDialog = true
    })


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        if (isLoading == true) {
            CircularProgressIndicator()
        }

        Button(
            onClick = {
                isButtonClicked = true
                authViewModel.login(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Don't have an account? Sign up.",
            modifier = Modifier.clickable { onNavigateToSignUp() }
        )

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



    val authResult = result
    LaunchedEffect(authResult) {
        if (isButtonClicked) {
            if (result is result.Success) {
                onSignInSuccess()
            } else if (result is result.Error) {
                Toast.makeText(
                    context,
                    authViewModel.mapExceptionToErrorMessage((result as result.Error).exception),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



}