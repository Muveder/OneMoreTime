package com.example.onemoretime.data

import com.example.onemoretime.model.Usuario

interface UsuarioRepository {
    suspend fun insertUsuario(usuario: Usuario)
}

class OfflineUsuarioRepository(private val usuarioDao: UsuarioDao) : UsuarioRepository {
    override suspend fun insertUsuario(usuario: Usuario) {
        usuarioDao.insert(usuario)
    }
}
