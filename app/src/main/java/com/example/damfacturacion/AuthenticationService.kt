package com.example.damfacturacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class AuthenticationService {

    private val baseUrl = "https://ws.ordenaplus.com/api/"
    private var authToken: String? = null

    interface ApiService {
        @GET("Usuarios/{user}/{pass}")
        suspend fun authenticateUser(
            @Path("user") user: String,
            @Path("pass") pass: String
        ): Response<AuthResponse>

        @GET("{endpoint}")
        suspend fun getData(
            @Path("endpoint") endpoint: String,
            @Header("Authorization") auth: String
        ): Response<*>
    }

    data class AuthResponse(val token: String)

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun login(user: String, pass: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = apiService.authenticateUser(user, pass)

            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse != null && authResponse.token.isNotEmpty()) { // Verifica si el token no está vacío
                    authToken = authResponse.token
                    return@withContext true // Autenticación exitosa
                } else {
                    // Manejo específico para respuesta exitosa pero sin token (usuario no encontrado)
                    println("Respuesta exitosa pero sin token. Usuario o clave incorrectos.")
                    return@withContext false
                }

            } else {
                //Manejo de errores de la API
                val errorBody = response.errorBody()?.string()
                println("Código de error: ${response.code()}")
                println("Mensaje de error: ${response.message()}")
                if(errorBody != null){
                    println("Cuerpo del error: $errorBody")
                }
                return@withContext false
            }
        } catch (e: Exception) {
            println("Excepción durante el login: ${e.message}")
            return@withContext false
        }
    }


    fun getAuthToken(): String? {
        return authToken
    }

    suspend fun getData(endpoint: String): Response<*>? = withContext(Dispatchers.IO) {
        try {
            val token = getAuthToken()
            token?.let {
                apiService.getData(endpoint, "Bearer $it")
            } ?: run {
                println("Token nulo. Inicie sesión primero.")
                null
            }
        } catch (e: Exception) {
            println("Excepción al obtener datos: ${e.message}")
            null
        }
    }
}