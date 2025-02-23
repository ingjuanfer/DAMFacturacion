package com.example.damfacturacion.interfaces

import com.example.damfacturacion.model.Cliente
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ClienteService {
    @POST("Clientes/CreateCliente")
    fun agregarCliente(
        @Header("Authorization") token: String,
        @Body cliente: Cliente
    ): Call<Void>
}