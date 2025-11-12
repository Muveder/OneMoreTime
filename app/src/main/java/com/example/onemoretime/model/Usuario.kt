package com.example.onemoretime.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    // Hacemos que el nombre y el correo sean únicos para evitar duplicados
    indices = [Index(value = ["nombre"], unique = true), Index(value = ["correo"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String, // Considera encriptar esto en una app real
    val direccion: String,
    val karma: Int = 0,
    val fechaRegistro: Long = System.currentTimeMillis(),
    val avatarUrl: String? = null
)
