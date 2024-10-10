package com.example.proyecto.DAO

import androidx.room.*
import androidx.room.Insert
import androidx.room.Query
import com.example.proyecto.Model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user:User)

    @Query("SELECT * FROM users")
    suspend fun getAllUser(): List<User>

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteById(userId: Int):Int

    @Query("UPDATE users SET nombre = :nombre, apellido = :apellido, edad = :edad WHERE id = :userId")
    suspend fun update(userId: Int, nombre: String, apellido: String, edad: Int): Int
}