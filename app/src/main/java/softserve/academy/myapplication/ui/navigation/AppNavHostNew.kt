package softserve.academy.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import softserve.academy.myapplication.ui.UserViewModel
import softserve.academy.myapplication.ui.screens.UserCreateScreen
import softserve.academy.myapplication.ui.screens.UserEditScreen
import softserve.academy.myapplication.ui.screens.UserListScreen


@Composable
fun AppNavHostNew(modifier: Modifier = Modifier) {
    val backStack = remember { mutableStateListOf<NavHostDestination>(UserListKey) }
    val viewModel: UserViewModel = hiltViewModel()

    fun back() = backStack.removeLastOrNull()

    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->
            when (key) {
                is UserListKey -> NavEntry(key) {
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
                    UserEditScreen(
                        userId = key.userId,
                        onBack = ::back,
                        viewModel = viewModel
                    )
                }
                is UserCreateKey -> NavEntry(
                    key = key,
                    metadata = slideHorizontalAnimation
                ) {
                    UserCreateScreen(
                        onBack = ::back,
                        viewModel = viewModel
                    )
                }
            }
        },
        modifier = modifier
    )
}
