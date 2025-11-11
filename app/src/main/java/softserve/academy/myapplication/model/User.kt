package softserve.academy.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val avatar: String,
    val createdAt: String
)
