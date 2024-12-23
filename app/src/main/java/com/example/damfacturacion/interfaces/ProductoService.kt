package com.example.damfacturacion.interfaces

import retrofit2.http.GET
import retrofit2.http.Headers
import com.example.damfacturacion.model.Producto

interface ProductoService {

    @Headers("Authorization: Bearer 1R78zNuBKvVDZENxrtwaSg==")
    @GET("Productos")
    suspend fun getProductos(): List<Producto>
}