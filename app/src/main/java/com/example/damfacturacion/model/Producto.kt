package com.example.damfacturacion.model

import java.math.BigDecimal
import java.util.Date
import com.google.gson.annotations.SerializedName

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

data class ListaProductosBuscados(
    @SerializedName("codProducto")
    val codProducto: String,

    @SerializedName("nombreProducto")
    val nombreProducto: String?,

//    @SerializedName("activo")
//    val activo: Boolean?,

//    @SerializedName("unidadMedida")
//    val unidadMedida: String?,

    @SerializedName("iva")
    val iva: Double?,

    @SerializedName("precio")
    val precio: Double?,

//    @SerializedName("fechaCreacion")
//    val fechaCreacion: String?,
//
//    @SerializedName("link_Image")
//    val linkImage: String?
)