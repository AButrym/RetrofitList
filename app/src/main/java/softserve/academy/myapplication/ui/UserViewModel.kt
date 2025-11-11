package softserve.academy.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import softserve.academy.myapplication.data.UserRepository
import softserve.academy.myapplication.model.User
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val data = repository.getUsers()
                _users.value = data.sortedByDescending { it.id.toIntOrNull() ?: Int.MIN_VALUE }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteUser(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteUser(id)
                _users.value = _users.value.filterNot { it.id == id }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateUserName(id: String, name: String, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                val updated = repository.updateUserName(id, name)
                _users.value = _users.value.map { if (it.id == id) updated else it }
                onDone()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun createUser(name: String, avatar: String, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                val created = repository.createUser(name, avatar)
                _users.value = (listOf(created) + _users.value).sortedByDescending { it.id.toIntOrNull() ?: Int.MIN_VALUE }
                onDone()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun consumeError() { _error.value = null }
}
