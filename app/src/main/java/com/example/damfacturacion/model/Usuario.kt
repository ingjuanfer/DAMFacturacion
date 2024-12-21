package com.example.damfacturacion.model

data class Usuario(
    val dni: String,
    val nombre: String,
    val userSesion: String,
    val claveSesion: String,
    val fechaRegistro: String,
    val activo: Boolean,
    val tel_Movil: String,
    val email: String
)