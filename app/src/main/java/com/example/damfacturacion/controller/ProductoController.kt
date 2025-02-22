package com.example.damfacturacion.controller

import com.example.damfacturacion.interfaces.ApiService
import com.example.damfacturacion.model.Producto
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

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    fun agregarProducto(token: String, producto: Producto, callback: (Boolean, String) -> Unit) {
        val call = apiService.agregarProducto("Bearer $token", producto)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true, "Producto agregado correctamente")
                } else {
                    callback(false, "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false, "Error de conexión: ${t.message}")
            }
        })
    }
}
