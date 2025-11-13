package softserve.academy.myapplication.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.NavKey

sealed interface NavHostDestination

@Serializable
object UserListKey : NavKey, NavHostDestination

@Serializable
data class UserEditKey(val userId: String) : NavKey, NavHostDestination

@Serializable
object UserCreateKey : NavKey, NavHostDestination
