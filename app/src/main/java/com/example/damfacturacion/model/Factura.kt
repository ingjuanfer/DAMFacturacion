package com.example.damfacturacion.model

import java.math.BigDecimal
import java.util.Date

data class FacturaEncabezado(
    val NIF: String,
    val TipoDcto: String = "FE",
    val DNI_Usuario_Reg: String
)

data class FacturaEncabezadoRespuesta(
    val mensaje: String,
    val nroDcto: Int
)

data class FacturaDetalle(
    val nroDcto: Int,
    val tipoDcto: String = "FE",
    val codProducto: String,
    val cantidad: Double,
    val precio_Unitario: Double,
    val iva: Double,
    val irpf: Double = 0.0
)