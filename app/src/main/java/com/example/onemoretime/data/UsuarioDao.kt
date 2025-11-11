package com.example.onemoretime.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.onemoretime.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usuario: Usuario)

    @Query("SELECT * from usuarios WHERE nombre = :nombre")
    fun getUsuarioPorNombre(nombre: String): Flow<Usuario>

    // Nueva función para validar el login
    @Query("SELECT * from usuarios WHERE correo = :correo LIMIT 1")
    suspend fun getUsuarioPorCorreo(correo: String): Usuario?
}
