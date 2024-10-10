package com.example.proyecto.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyecto.Model.User
import com.example.proyecto.Repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserApp(userRepository: UserRepository) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellido by rememberSaveable { mutableStateOf("") }
    var edad by rememberSaveable { mutableStateOf("") }
    var users: List<User> by rememberSaveable { mutableStateOf(listOf<User>()) }
    var userIdToUpdate: Int? by rememberSaveable { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Variables para los mensajes de error
    var nombreError by rememberSaveable { mutableStateOf(false) }
    var apellidoError by rememberSaveable { mutableStateOf(false) }
    var edadError by rememberSaveable { mutableStateOf(false) }
    var generalError by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // CELDA NOMBRE
        TextField(
            value = nombre,
            onValueChange = {
                nombre = it
                nombreError = nombre.isBlank() || nombre.length > 15 || !nombre.all { it.isLetterOrDigit() }
            },
            label = { Text("Nombre") },
            isError = nombreError,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = if (nombreError) Color(0xFFE9FFE6) else MaterialTheme.colorScheme.surface
            )
        )
        if (nombreError) {
            Text(
                text = "Nombre debe ser alfanumérico y no mayor a 15 caracteres.",
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(Color(0xFFFFE6E6))
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.small)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CELDA APELLIDO
        TextField(
            value = apellido,
            onValueChange = {
                apellido = it
                apellidoError = apellido.isBlank() || apellido.length > 15 || !apellido.all { it.isLetterOrDigit() }
            },
            label = { Text("Apellido") },
            isError = apellidoError,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors(
                containerColor = if (apellidoError) Color(0xFFFFE6E6) else MaterialTheme.colorScheme.surface
            )
        )

        if (apellidoError) {
            Text(
                text = "Apellido debe ser alfanumérico y no mayor a 15 caracteres.",
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(Color(0xFFFFE6E6))
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.small)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CELDAD EDAD
        TextField(
            value = edad,
            onValueChange = {
                edad = it
                edadError = edad.isBlank() || edad.length > 2 || !edad.all { it.isDigit() }
            },
            label = { Text("Edad") },
            isError = edadError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors(
                containerColor = if (edadError) Color(0xFFFFE6E6) else MaterialTheme.colorScheme.surface
            )
        )
        if (edadError) {
            Text(
                text = "Edad debe ser numérica y no mayor a 2 caracteres.",
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(Color(0xFFFFE6E6))
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.small)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de Registrar y Listar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                if (nombreError || apellidoError || edadError || nombre.isBlank() || apellido.isBlank() || edad.isBlank()) {
                    generalError = "Todos los campos son obligatorios"
                } else {
                    val user = User(
                        nombre = nombre,
                        apellido = apellido,
                        edad = edad.toIntOrNull() ?: 0
                    )

                    scope.launch {
                        withContext(Dispatchers.IO) {
                            if (userIdToUpdate != null) {
                                userRepository.update(userIdToUpdate!!, nombre, apellido, edad.toIntOrNull() ?: 0)
                            } else {
                                userRepository.insert(user)
                            }
                        }
                        Toast.makeText(context, if (userIdToUpdate != null) "Usuario Actualizado" else "Usuario Registrado", Toast.LENGTH_SHORT).show()
                        nombre = ""
                        apellido = ""
                        edad = ""
                        userIdToUpdate = null
                        users = withContext(Dispatchers.IO) {
                            userRepository.getAllUsers()
                        }
                    }
                    generalError = ""
                }
            }) {
                Text(if (userIdToUpdate != null) "Actualizar" else "Registrar")
            }

            Button(onClick = {
                scope.launch {
                    users = withContext(Dispatchers.IO) {
                        userRepository.getAllUsers()
                    }
                }
            }) {
                Text("Listar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (generalError.isNotBlank()) {
            Text(
                text = generalError,
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(Color(0xFFFFE6E6))
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.small)
            )
        }

        // Implementar scroll para la lista de usuarios
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(users) { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Mostrar datos del usuario
                    Text(text = "${user.nombre} ${user.apellido} Edad: ${user.edad}")

                    Row {
                        // Icono de editar
                        IconButton(onClick = {
                            nombre = user.nombre
                            apellido = user.apellido
                            edad = user.edad.toString()
                            userIdToUpdate = user.id
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Usuario")
                        }

                        // Icono de eliminar
                        IconButton(onClick = {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    userRepository.deleteById(user.id)
                                }
                                Toast.makeText(context, "Usuario Eliminado", Toast.LENGTH_SHORT).show()

                                users = withContext(Dispatchers.IO) {
                                    userRepository.getAllUsers()
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar Usuario")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}