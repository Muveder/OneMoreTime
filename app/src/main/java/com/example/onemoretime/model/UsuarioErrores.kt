package com.example.onemoretime.model

data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null,
    val registro: String? = null // Nuevo campo para errores generales de registro
)
