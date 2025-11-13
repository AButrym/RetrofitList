package softserve.academy.myapplication.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.NavKey

@Serializable
object UserListKey : NavKey

@Serializable
data class UserEditKey(val userId: String) : NavKey

@Serializable
object UserCreateKey : NavKey