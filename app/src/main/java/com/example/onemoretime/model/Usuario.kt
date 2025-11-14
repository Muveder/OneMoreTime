package com.example.onemoretime.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["nombre"], unique = true), Index(value = ["correo"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val direccion: String,
    val karma: Int = 0,
    val fechaRegistro: Long = System.currentTimeMillis(),
    val avatarUrl: String? = null,
    // Nuevos campos para el perfil
    val nombreReal: String? = null,
    val cumpleanos: Long? = null
)
