package com.example.damfacturacion.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.damfacturacion.interfaces.Autenticacion
import com.example.damfacturacion.interfaces.ProductoService

object RetrofitClient {
    private const val BASE_URL = "https://ws.ordenaplus.com/api/"

    // Inicializamos Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Método para obtener la instancia de ApiService
    fun getApiService(): Autenticacion {
        return retrofit.create(Autenticacion::class.java)
    }

    // Método para obtener la instancia de ProductoService
    fun getProductoService(): ProductoService {
        return retrofit.create(ProductoService::class.java)
    }
}
