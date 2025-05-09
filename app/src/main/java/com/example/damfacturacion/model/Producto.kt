package com.example.damfacturacion.model

import java.math.BigDecimal
import java.util.Date

data class Producto(
    val codProducto: String,
    val nombreProducto: String?,
    val activo: Boolean?,
    val unidadMedida: String?,
    val iva: Double?,
    val precio: Double?,
    val linkImage: String?
)


data class ListaProductos(
    val codProducto: String,
    val nombreProducto: String?,
    val activo: Boolean?,
    val unidadMedida: String?,
    val iva: Double?,
    val precio: Double?,
    val fechaCreacion: Date?,
    val linkImage: String?
)

data class ProductoSeleccionado(
    val codProducto: String,
    val nombreProducto: String,
    val precio: Double,
    val cantidad: Int,
    val iva: Double
)