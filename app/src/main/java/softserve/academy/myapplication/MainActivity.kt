package softserve.academy.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import softserve.academy.myapplication.ui.UserViewModel
import softserve.academy.myapplication.ui.screens.UserCreateScreen
import softserve.academy.myapplication.ui.screens.UserEditScreen
import softserve.academy.myapplication.ui.screens.UserListScreen
import softserve.academy.myapplication.ui.theme.MyApplicationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    fun back() = navController.popBackStack()

    NavHost(navController = navController, startDestination = "list", modifier = modifier) {
        composable("list") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("list")
            }
            val sharedVm: UserViewModel = hiltViewModel(parentEntry)
            UserListScreen(
                onEdit = { id -> navController.navigate("edit/$id") },
                onCreate = { navController.navigate("create") },
                viewModel = sharedVm
            )
        }
        composable(
            route = "edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("list")
            }
            val sharedVm: UserViewModel = hiltViewModel(parentEntry)
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            UserEditScreen(
                userId = id,
                onDone = ::back,
                onCancel = ::back,
                viewModel = sharedVm
            )
        }
        composable("create") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("list")
            }
            val sharedVm: UserViewModel = hiltViewModel(parentEntry)
            UserCreateScreen(
                onDone = ::back,
                onCancel = ::back,
                viewModel = sharedVm
            )
        }
    }
}