package com.example.damfacturacion.interfaces

import com.example.damfacturacion.model.Cliente
import com.example.damfacturacion.model.ListaProductosBuscados
import com.example.damfacturacion.model.Producto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.*

interface ProductoService {

    @POST("Productos/CreateProducto")
    fun agregarProducto(
        @Header("Authorization") token: String,
        @Body producto: Producto
    ): Call<Void>

    @PUT("Productos/{codProducto}")
    fun actualizarProducto(
        @Header("Authorization") token: String,
        @Path("codProducto") codProducto: String,
        @Body producto: Producto
    ): Call<Void>

    @DELETE("Productos/{codProducto}")
    fun eliminarProducto(
        @Header("Authorization") token: String,
        @Path("codProducto") codProducto: String
    ): Call<Void>

    @GET("Productos/{codProducto}")
    fun getProductoPorCodigo(
        @Header("Authorization") token: String,
        @Path("codProducto") codProducto: String
    ): Call<List<Producto>> // Se devuelve una lista porque el JSON es un array

    @GET("Productos/Consulta_Nombres/{nombre}")
    fun buscarProductosPorNombre(
        @Header("Authorization") token: String,
        @Path("nombre") nombre: String
    ): Call<List<ListaProductosBuscados>>


}