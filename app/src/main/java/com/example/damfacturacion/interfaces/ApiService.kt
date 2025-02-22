package com.example.damfacturacion.interfaces

import com.example.damfacturacion.model.Producto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("Productos/CreateProducto")
    fun agregarProducto(
        @Header("Authorization") token: String,
        @Body producto: Producto
    ): Call<Void>
}


