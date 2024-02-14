package com.example.chatroomapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatroomapp.screen.ChatMeScreen
import com.example.chatroomapp.screen.ChatRoomListScreen
import com.example.chatroomapp.screen.ChatScreen
import com.example.chatroomapp.screen.ExitConfirmationListener
import com.example.chatroomapp.screen.LoginScreen
import com.example.chatroomapp.screen.SignUpScreen
import com.example.chatroomapp.ui.theme.ChatroomappTheme
import com.example.chatroomapp.viewmodel.AuthViewModel
import com.example.chatroomapp.viewmodel.RoomViewModel

class MainActivity : ComponentActivity(), ExitConfirmationListener {
    private var isExitDialogVisible by mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            val roomViewModel: RoomViewModel = viewModel()

            ChatroomappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(LocalOnBackPressedDispatcherOwner provides this) {
                        NavigationGraph(
                            navController = navController,
                            authViewModel = authViewModel,
                            roomViewModel = roomViewModel,
                            exitConfirmationListener = this@MainActivity as ExitConfirmationListener // Explicitly cast MainActivity to ExitConfirmationListener
                        )
                    }
                }
            }
        }
    }

    override fun onConfirmExit() {
        // Handle exit confirmation here
        // Perform action to exit the app or handle as required
        // For example, you can call finish()
        finish()
    }

    override fun onBackPressed() {
        isExitDialogVisible = !isExitDialogVisible
        super.onBackPressed()
    }
}

//NavigationGraph(navController = navController, authViewModel = authViewModel, roomViewModel = roomViewModel)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    authViewModel: AuthViewModel,
    roomViewModel:  RoomViewModel,
    navController: NavHostController,
    exitConfirmationListener: ExitConfirmationListener
) {
    NavHost(
        navController = navController,
//        startDestination = if (roomViewModel.isloggedin) Screen.ChatRoomsScreen.route else Screen.ChatMeScreen.route
       startDestination = Screen.ChatMeScreen.route
    ) {

        composable(Screen.ChatMeScreen.route) {
            ChatMeScreen(
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) },
                onNavigateToSignUp = {navController.navigate(Screen.SignupScreen.route)},
                exitConfirmationListener = exitConfirmationListener
            )
        }


        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) },
                exitConfirmationListener = exitConfirmationListener
            )
        }

        composable(Screen.ChatRoomsScreen.route) {
            ChatRoomListScreen (
                onJoinClicked ={ navController.navigate("${Screen.ChatScreen.route}/${it.id}")},
                onLogoutClicked = {
                    roomViewModel.logout() // Call the logout function from ViewModel
                    navController.navigate(Screen.LoginScreen.route) // Navigate to login screen
                },
                exitConfirmationListener = exitConfirmationListener
            )
            }


        composable("${Screen.ChatScreen.route}/{roomId}") {
            val roomId: String = it
                .arguments?.getString("roomId") ?: ""
            ChatScreen(roomId = roomId)
        }

        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.SignupScreen.route) },
                onSignInSuccess = { navController.navigate(Screen.ChatRoomsScreen.route)},
                exitConfirmationListener = exitConfirmationListener
            )

        }
    }
}


