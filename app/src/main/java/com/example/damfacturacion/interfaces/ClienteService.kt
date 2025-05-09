package com.example.damfacturacion.interfaces

import com.example.damfacturacion.model.Cliente
import com.example.damfacturacion.model.Producto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClienteService {

    @POST("Clientes/CreateCliente")
    fun agregarCliente(
        @Header("Authorization") token: String,
        @Body cliente: Cliente
    ): Call<Void>

    @PUT("Clientes/{nIF}")
    fun actualizarCliente(
        @Header("Authorization") token: String,
        @Path("nIF") nIF: String,
        @Body cliente: Cliente
    ): Call<Void>

    @DELETE("Clientes/{nIF}")
    fun eliminarCliente(
        @Header("Authorization") token: String,
        @Path("nIF") nIF: String
    ): Call<Void>

    @GET("Clientes/{nIF}")
    fun getClientePorNIF(
        @Header("Authorization") token: String,
        @Path("nIF") nIF: String
    ): Call<List<Cliente>> // Se devuelve una lista porque el JSON es un array

    @GET("Clientes/Consulta_Nombres/{nombre}")
    fun buscarClientesPorNombre(
        @Header("Authorization") token: String,
        @Path("nombre") nombre: String
    ): Call<List<Cliente>>


}