package com.example.damfacturacion.controller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damfacturacion.api.RetrofitClient
import com.example.damfacturacion.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginController : ViewModel() {

    // Modificar el método login para aceptar un parámetro adicional 'authorizationHeader'
    fun login(username: String, password: String, authorizationHeader: String, onSuccess: (Usuario) -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Llamamos al servicio API para obtener la respuesta, pasando el encabezado de autorización
                val response = RetrofitClient.getApiService().login(username, password, authorizationHeader)

                // Verificamos la respuesta
                handleResponse(response, onSuccess, onFailure)
            } catch (e: Exception) {
                // Manejo de errores si no se puede conectar
                onFailure("Error: ${e.message}")
            }
        }
    }

    private suspend fun handleResponse(response: Response<List<Usuario>>, onSuccess: (Usuario) -> Unit, onFailure: (String) -> Unit) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                val users = response.body()
                if (users.isNullOrEmpty()) {
                    onFailure("Usuario o Clave inválidos!")
                } else {
                    // Si se encuentra el usuario, lo pasamos al callback de éxito
                    val user = users[0]
                    onSuccess(user)
                }
            } else {
                onFailure("Error en la respuesta de la API")
            }
        }
    }
}
