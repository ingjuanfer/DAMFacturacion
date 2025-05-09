package com.example.damfacturacion.interfaces

import com.example.damfacturacion.model.FacturaEncabezado
import com.example.damfacturacion.model.FacturaEncabezadoRespuesta
import com.example.damfacturacion.model.FacturaDetalle
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FacturaService {
    @POST("Trade/CreateTrade")
    fun crearEncabezadoFactura(
        @Header("Authorization") token: String,
        @Body encabezado: FacturaEncabezado
    ): Call<FacturaEncabezadoRespuesta>

    @POST("MvTrade/CreateMvTrade")
    fun crearDetalleFactura(
        @Header("Authorization") token: String,
        @Body detalle: FacturaDetalle
    ): Call<Void>
}
