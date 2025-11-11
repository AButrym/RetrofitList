package softserve.academy.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skydoves.landscapist.coil3.CoilImage
import softserve.academy.myapplication.ui.UserViewModel

private const val DEFAULT_AVATAR = "https://avatars.githubusercontent.com/u/9919?s=400&v=4"

@Composable
fun UserCreateScreen(
    onDone: () -> Unit,
    onCancel: () -> Unit,
    viewModel: UserViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Create User", style = MaterialTheme.typography.headlineSmall)
        HorizontalDivider()
        CoilImage(
            imageModel = { DEFAULT_AVATAR },
            modifier = Modifier.height(120.dp)
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { viewModel.createUser(name, DEFAULT_AVATAR, onDone) }, enabled = name.isNotBlank()) {
                Text("Submit")
            }
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}
