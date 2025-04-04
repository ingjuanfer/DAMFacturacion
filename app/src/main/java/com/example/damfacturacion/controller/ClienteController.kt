package com.example.damfacturacion.controller

import android.util.Log
import com.example.damfacturacion.controller.ProductoController.ErrorResponse
import com.example.damfacturacion.interfaces.ClienteService
import com.example.damfacturacion.model.Cliente
import com.example.damfacturacion.model.Producto
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClienteController {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://ws.ordenaplus.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val clienteService: ClienteService = retrofit.create(ClienteService::class.java)

    fun agregarCliente(token: String, cliente: Cliente, callback: (Boolean, String) -> Unit) {
        val call = clienteService.agregarCliente("Bearer $token", cliente)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true, "Cliente creado correctamente")
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

    fun obtenerCliente(token: String, nIF: String, callback: (Cliente?, String) -> Unit) {
        val call = clienteService.getClientePorNIF("Bearer $token", nIF)

        call.enqueue(object : Callback<List<Cliente>> {
            override fun onResponse(call: Call<List<Cliente>>, response: Response<List<Cliente>>) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    callback(response.body()!![0], "Cliente encontrado")
                } else {
                    callback(null, "Cliente no encontrado")
                }
            }

            override fun onFailure(call: Call<List<Cliente>>, t: Throwable) {
                callback(null, "Error de conexión: ${t.message}")
            }
        })
    }

    fun eliminarCliente(token: String, nIF: String, callback: (Boolean , String ) -> Unit) {
        val call = clienteService.eliminarCliente("Bearer $token", nIF)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true, "Cliente eliminado correctamente")
                } else {
                    val errorMessage = parseError(response)
                    callback(false, errorMessage)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false, "Error de conexión: ${t.message}")
            }
        })
    }

    fun actualizarCliente(token: String, cliente: Cliente, callback: (Boolean, String) -> Unit) {
        val call = clienteService.actualizarCliente("Bearer $token", cliente.nif, cliente)

        Log.d("ClienteService", "Call generado: $call")

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true, "Cliente actualizado correctamente")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        val errorJson = Gson().fromJson(errorBody, com.example.damfacturacion.controller.ProductoController.ErrorResponse::class.java)
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

    fun getClientePorNIF(token: String, nIF: String, callback: (Cliente?, String) -> Unit) {
        val call = clienteService.getClientePorNIF("Bearer $token", nIF )

        call.enqueue(object : Callback<List<Cliente>> {
            override fun onResponse(call: Call<List<Cliente>>, response: Response<List<Cliente>>) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    callback(response.body()!![0], "Cliente encontrado")
                } else {
                    callback(null, "Cliente no encontrado")
                }
            }

            override fun onFailure(call: Call<List<Cliente>>, t: Throwable) {
                callback(null, "Error de conexión: ${t.message}")
            }
        })
    }


    // Método para parsear errores
    private fun parseError(response: Response<*>): String {
        val errorBody = response.errorBody()?.string()
        return try {
            val errorJson = Gson().fromJson(errorBody, com.example.damfacturacion.controller.ClienteController.ErrorResponse::class.java)
            errorJson.mensaje ?: "Error desconocido"
        } catch (e: Exception) {
            "Error desconocido (${response.code()})"
        }
    }


    // Clase para parsear el error JSON
    data class ErrorResponse(
        @SerializedName("mensaje") val mensaje: String
    )
}
