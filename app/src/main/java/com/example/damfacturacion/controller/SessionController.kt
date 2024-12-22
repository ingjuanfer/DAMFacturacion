package com.example.damfacturacion.controller

import android.content.Context
import android.content.SharedPreferences
import com.example.damfacturacion.model.Usuario

class SessionController(private val context: Context) {

    private val PREF_NAME = "UserSession"

    // Método para guardar la sesión con los datos del Usuario
    fun saveSession(usuario: Usuario) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Guardar cada dato del Usuario individualmente
        editor.putString("DNI", usuario.dni)
        editor.putString("NOMBRE", usuario.nombre)
        editor.putString("USER_SESION", usuario.userSesion)
        editor.putString("CLAVE_SESION", usuario.claveSesion)
        editor.putString("FECHA_REGISTRO", usuario.fechaRegistro)
        editor.putBoolean("ACTIVO", usuario.activo)
        editor.putString("TEL_MOVIL", usuario.tel_Movil)
        editor.putString("EMAIL", usuario.email)

        // Aplicar los cambios
        editor.apply()
    }

    // Método para obtener los datos del Usuario desde SharedPreferences
    fun getSession(): Usuario? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // Obtener los valores de SharedPreferences
        val dni = sharedPreferences.getString("DNI", null)
        val nombre = sharedPreferences.getString("NOMBRE", null)
        val userSesion = sharedPreferences.getString("USER_SESION", null)
        val claveSesion = sharedPreferences.getString("CLAVE_SESION", null)
        val fechaRegistro = sharedPreferences.getString("FECHA_REGISTRO", null)
        val activo = sharedPreferences.getBoolean("ACTIVO", false)
        val telMovil = sharedPreferences.getString("TEL_MOVIL", null)
        val email = sharedPreferences.getString("EMAIL", null)

        // Verificar si los datos están disponibles
        if (dni != null && nombre != null && userSesion != null && claveSesion != null && fechaRegistro != null &&
            telMovil != null && email != null) {
            return Usuario(dni, nombre, userSesion, claveSesion, fechaRegistro, activo, telMovil, email)
        }
        return null  // Si no hay sesión guardada, retornar null
    }

    // Método para borrar la sesión
    fun clearSession() {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()  // Borrar todas las preferencias
        editor.apply()  // Aplicar los cambios
    }
}
