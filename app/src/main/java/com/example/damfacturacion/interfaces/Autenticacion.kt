package com.example.damfacturacion.interfaces

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.damfacturacion.model.Usuario
import retrofit2.http.Headers

interface Autenticacion {
    // Endpoint para autenticar el usuario con la clave
    @Headers("Authorization: Bearer 1R78zNuBKvVDZENxrtwaSg==")
    @GET("Usuarios/{username}/{password}")
    suspend fun login(
        @Path("username") username: String,
        @Path("password") password: String
    ): Response<List<Usuario>>
}
