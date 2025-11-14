package com.example.onemoretime.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.onemoretime.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usuario: Usuario): Long

    @Query("SELECT * from usuarios WHERE nombre = :nombre")
    fun getUsuarioPorNombre(nombre: String): Flow<Usuario>

    @Query("SELECT * from usuarios WHERE correo = :correo LIMIT 1")
    suspend fun getUsuarioPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE nombre = :credential OR correo = :credential LIMIT 1")
    suspend fun findUserByCredential(credential: String): Usuario?

    // Nueva función para actualizar un usuario
    @Update
    suspend fun update(usuario: Usuario)
}
