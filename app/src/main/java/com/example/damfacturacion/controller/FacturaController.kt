package com.example.damfacturacion.controller

import com.example.damfacturacion.model.FacturaEncabezado
import com.example.damfacturacion.model.FacturaEncabezadoRespuesta
import com.example.damfacturacion.model.FacturaDetalle
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.example.damfacturacion.interfaces.ProductoService
import com.example.damfacturacion.model.Producto
import com.google.gson.annotations.SerializedName
import com.example.damfacturacion.interfaces.FacturaService

class FacturaController {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://ws.ordenaplus.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val facturaService: FacturaService = retrofit.create(FacturaService::class.java)

    fun crearEncabezadoFactura(
        token: String,
        encabezado: FacturaEncabezado,
        callback: (Int?, String) -> Unit
    ) {
        val call = facturaService.crearEncabezadoFactura("Bearer $token", encabezado)

        call.enqueue(object : Callback<FacturaEncabezadoRespuesta> {
            override fun onResponse(
                call: Call<FacturaEncabezadoRespuesta>,
                response: Response<FacturaEncabezadoRespuesta>
            ) {
                if (response.isSuccessful) {
                    val respuesta = response.body()
                    callback(respuesta?.nroDcto, respuesta?.mensaje ?: "Encabezado creado")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        val errorJson = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        errorJson.mensaje ?: "Error desconocido"
                    } catch (e: Exception) {
                        "Error desconocido (${response.code()})"
                    }
                    callback(null, errorMessage)
                }
            }

            override fun onFailure(call: Call<FacturaEncabezadoRespuesta>, t: Throwable) {
                callback(null, "Error de conexión: ${t.message}")
            }
        })
    }

    fun crearDetalleFactura(
        token: String,
        detalle: FacturaDetalle,
        callback: (Boolean, String) -> Unit
    ) {
        val call = facturaService.crearDetalleFactura("Bearer $token", detalle)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true, "Detalle creado correctamente")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        val errorJson = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        errorJson.mensaje ?: "Error desconocido"
                    } catch (e: Exception) {
                        "Error desconocido (${response.code()})"
                    }
                    callback(false, errorMessage)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false, "Error de conexión: ${t.message}")
            }
        })
    }

    // Clase para parsear el error JSON
    data class ErrorResponse(
        @SerializedName("mensaje") val mensaje: String
    )
}
