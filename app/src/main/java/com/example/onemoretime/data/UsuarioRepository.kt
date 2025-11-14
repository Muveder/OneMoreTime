package com.example.onemoretime.data

import com.example.onemoretime.model.Usuario
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    suspend fun insertUsuario(usuario: Usuario): Long
    fun getUsuarioPorNombreStream(nombre: String): Flow<Usuario>
    suspend fun getUsuarioPorCorreo(correo: String): Usuario?
    suspend fun findUserByCredential(credential: String): Usuario?
    suspend fun updateUsuario(usuario: Usuario) // <-- Nuevo método
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

    override suspend fun findUserByCredential(credential: String): Usuario? {
        return usuarioDao.findUserByCredential(credential)
    }

    override suspend fun updateUsuario(usuario: Usuario) {
        usuarioDao.update(usuario)
    }
}
