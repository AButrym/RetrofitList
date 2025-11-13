package softserve.academy.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import softserve.academy.myapplication.ui.UserViewModel
import softserve.academy.myapplication.ui.screens.UserCreateScreen
import softserve.academy.myapplication.ui.screens.UserEditScreen
import softserve.academy.myapplication.ui.screens.UserListScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    fun back() = navController.popBackStack()

    NavHost(navController = navController, startDestination = "list", modifier = modifier) {
        composable("list") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("list")
            }
            val sharedVm: UserViewModel =
                hiltViewModel(parentEntry)
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
            val sharedVm: UserViewModel =
                hiltViewModel(parentEntry)
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            UserEditScreen(
                userId = id,
                onBack = ::back,
                viewModel = sharedVm
            )
        }
        composable("create") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("list")
            }
            val sharedVm: UserViewModel =
                hiltViewModel(parentEntry)
            UserCreateScreen(
                onBack = ::back,
                viewModel = sharedVm
            )
        }
    }
}
