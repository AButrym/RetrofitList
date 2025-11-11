package softserve.academy.myapplication.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.PUT
import softserve.academy.myapplication.model.User

interface UserService {
    @GET("user")
    suspend fun getUsers(): List<User>

    @POST("user")
    suspend fun createUser(@Body body: CreateUserRequest): User

    @PUT("user/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body body: UpdateUserRequest): User

    @DELETE("user/{id}")
    suspend fun deleteUser(@Path("id") id: String)
}

@kotlinx.serialization.Serializable
data class CreateUserRequest(
    val name: String,
    val avatar: String
)

@kotlinx.serialization.Serializable
data class UpdateUserRequest(
    val name: String
)
