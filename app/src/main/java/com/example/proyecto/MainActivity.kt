package com.example.proyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proyecto.DAO.UserDao
import com.example.proyecto.Database.UserDatabase
import com.example.proyecto.Repository.UserRepository
import com.example.proyecto.screen.UserApp

class MainActivity : ComponentActivity() {

    private  lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = UserDatabase.getDatabase(applicationContext)
        userDao = db.userDao()
        userRepository = UserRepository(userDao) // Inicializa el Repositorio

        enableEdgeToEdge()
        setContent {
            UserApp(userRepository)
        }
    }
}