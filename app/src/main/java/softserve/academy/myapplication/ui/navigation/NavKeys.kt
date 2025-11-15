package softserve.academy.myapplication.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.NavKey

sealed interface NavHostDestination : NavKey

@Serializable
object UserListKey : NavHostDestination

@Serializable
data class UserEditKey(val userId: String) : NavHostDestination

@Serializable
object UserCreateKey : NavHostDestination
