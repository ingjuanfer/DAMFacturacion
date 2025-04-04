package com.example.damfacturacion.model

data class Cliente(
    val nif: String,
    val tipoIdentificacion: String?,
    val digitoControl: String?,
    val activo: Boolean?,
    val nombreEmpresa: String?,
    val telefono: String?,
    val movil: String?,
    val direccion: String?,
    val codigoPostal: String?,
    val email: String?,
    val codCiudad: String?,
    val codProvincia: String?,
    val codPais: String?
)


