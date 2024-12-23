package com.example.damfacturacion.model

import java.math.BigDecimal
import java.util.Date

data class Producto(
    val codProducto: String,
    val nombreProducto: String?,
    val activo: Boolean?,
    val unidadMedida: String?,
    val iva: BigDecimal?,
    val precio: BigDecimal?,
    val fechaCreacion: Date?,
    val linkImage: String?
)
