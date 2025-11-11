package softserve.academy.myapplication.data

import softserve.academy.myapplication.model.User
import softserve.academy.myapplication.network.CreateUserRequest
import softserve.academy.myapplication.network.UpdateUserRequest
import softserve.academy.myapplication.network.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val service: UserService
) {
    suspend fun getUsers(): List<User> = service.getUsers()
    suspend fun createUser(name: String, avatar: String): User =
        service.createUser(CreateUserRequest(name = name, avatar = avatar))

    suspend fun updateUserName(id: String, name: String): User =
        service.updateUser(id, UpdateUserRequest(name = name))

    suspend fun deleteUser(id: String) {
        service.deleteUser(id)
    }
}
