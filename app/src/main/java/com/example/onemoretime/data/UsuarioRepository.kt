package com.example.onemoretime.data

import com.example.onemoretime.model.Usuario
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    // Modificado para que devuelva el resultado de la inserción
    suspend fun insertUsuario(usuario: Usuario): Long
    fun getUsuarioPorNombreStream(nombre: String): Flow<Usuario>
    suspend fun getUsuarioPorCorreo(correo: String): Usuario?
}

class OfflineUsuarioRepository(private val usuarioDao: UsuarioDao) : UsuarioRepository {
    override suspend fun insertUsuario(usuario: Usuario): Long {
        return usuarioDao.insert(usuario)
    }

    override fun getUsuarioPorNombreStream(nombre: String): Flow<Usuario> {
        return usuarioDao.getUsuarioPorNombre(nombre)
    }

    override suspend fun getUsuarioPorCorreo(correo: String): Usuario? {
        return usuarioDao.getUsuarioPorCorreo(correo)
    }
}
