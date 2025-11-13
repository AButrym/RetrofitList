package softserve.academy.myapplication.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import softserve.academy.myapplication.ui.UserViewModel
import softserve.academy.myapplication.ui.screens.UserCreateScreen
import softserve.academy.myapplication.ui.screens.UserEditScreen
import softserve.academy.myapplication.ui.screens.UserListScreen


@Composable
fun AppNavHostNew(modifier: Modifier = Modifier) {
    val backStack = remember { mutableStateListOf<NavHostDestination>(UserListKey) }

    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->
            when (key) {
                is UserListKey -> NavEntry(key) {
                    val viewModel: UserViewModel = hiltViewModel()
                    UserListScreen(
                        onEdit = { id -> backStack += UserEditKey(id) },
                        onCreate = { backStack += UserCreateKey },
                        viewModel = viewModel
                    )
                }
                is UserEditKey -> NavEntry(
                    key = key,
                    metadata = slideVerticalAnimation
                ) {
                    val viewModel: UserViewModel = hiltViewModel()
                    UserEditScreen(
                        userId = key.userId,
                        onBack = { backStack.removeLastOrNull() },
                        viewModel = viewModel
                    )
                }
                is UserCreateKey -> NavEntry(
                    key = key,
                    metadata = slideHorizontalAnimation
                ) {
                    val viewModel: UserViewModel = hiltViewModel()
                    UserCreateScreen(
                        onBack = { backStack.removeLastOrNull() },
                        viewModel = viewModel
                    )
                }
            }
        },
        modifier = modifier
    )
}
