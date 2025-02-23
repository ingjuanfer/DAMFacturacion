package com.example.damfacturacion.controller

import com.example.damfacturacion.interfaces.ProductoService
import com.example.damfacturacion.model.Producto
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductoController {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://ws.ordenaplus.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val productoService: ProductoService = retrofit.create(ProductoService::class.java)

    fun agregarProducto(token: String, producto: Producto, callback: (Boolean, String) -> Unit) {
        val call = productoService.agregarProducto("Bearer $token", producto)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true, "Producto creado correctamente")
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
