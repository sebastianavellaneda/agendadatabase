package com.example.proyecto.Repository

import com.example.proyecto.DAO.UserDao
import com.example.proyecto.Model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: User){
        userDao.insert(user)
    }

    suspend fun getAllUsers(): List<User>{
        return userDao.getAllUser()
    }

    suspend fun deleteById(userId: Int): Int {
        return userDao.deleteById(userId)
    }

    suspend fun update(userId: Int, nombre: String, apellido: String, edad: Int): Int {
        return userDao.update(userId, nombre, apellido, edad)
    }
}