package com.example.damfacturacion.controller

import android.content.Context
import android.content.SharedPreferences
import com.example.damfacturacion.model.Usuario
import com.example.damfacturacion.model.Cliente
import com.example.damfacturacion.model.ListaProductosBuscados
import com.example.damfacturacion.model.Producto
import com.google.gson.Gson

class SessionController(private val context: Context) {

    private val PREF_NAME_USER = "UserSession"
    private val PREF_NAME_CLIENT = "ClientSession"
    private val PREF_NAME_PRODUCT = "ProductSession"

    // Métodos para gestionar sesión de usuario
    fun saveSession(usuario: Usuario) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME_USER, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("DNI", usuario.dni)
        editor.putString("NOMBRE", usuario.nombre)
        editor.putString("USER_SESION", usuario.userSesion)
        editor.putString("CLAVE_SESION", usuario.claveSesion)
        editor.putString("FECHA_REGISTRO", usuario.fechaRegistro)
        editor.putBoolean("ACTIVO", usuario.activo)
        editor.putString("TEL_MOVIL", usuario.tel_Movil)
        editor.putString("EMAIL", usuario.email)
        editor.apply()
    }

    fun getSession(): Usuario? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME_USER, Context.MODE_PRIVATE)
        val dni = sharedPreferences.getString("DNI", null)
        val nombre = sharedPreferences.getString("NOMBRE", null)
        val userSesion = sharedPreferences.getString("USER_SESION", null)
        val claveSesion = sharedPreferences.getString("CLAVE_SESION", null)
        val fechaRegistro = sharedPreferences.getString("FECHA_REGISTRO", null)
        val activo = sharedPreferences.getBoolean("ACTIVO", false)
        val telMovil = sharedPreferences.getString("TEL_MOVIL", null)
        val email = sharedPreferences.getString("EMAIL", null)

        if (dni != null && nombre != null && userSesion != null && claveSesion != null && fechaRegistro != null &&
            telMovil != null && email != null) {
            return Usuario(dni, nombre, userSesion, claveSesion, fechaRegistro, activo, telMovil, email)
        }
        return null
    }

    fun clearSession() {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME_USER, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    // Métodos para gestionar sesión de cliente
    fun saveClientSession(cliente: Cliente) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME_CLIENT, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val clienteJson = Gson().toJson(cliente)
        editor.putString("CLIENT_DATA", clienteJson)
        editor.apply()
    }

    fun getClientSession(): Cliente? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME_CLIENT, Context.MODE_PRIVATE)
        val clienteJson = sharedPreferences.getString("CLIENT_DATA", null)

        return if (clienteJson != null) {
            Gson().fromJson(clienteJson, Cliente::class.java)
        } else {
            null
        }
    }

    fun clearClientSession() {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME_CLIENT, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


    // Métodos para gestionar sesión de producto
    fun saveProductSession(producto: ListaProductosBuscados) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME_PRODUCT, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val productoJson = Gson().toJson(producto)
        editor.putString("CLIENT_DATA", productoJson)
        editor.apply()
    }

    fun getProductSession(): Producto? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME_PRODUCT, Context.MODE_PRIVATE)
        val productoJson = sharedPreferences.getString("CLIENT_DATA", null)

        return if (productoJson != null) {
            Gson().fromJson(productoJson, Producto::class.java)
        } else {
            null
        }
    }

    fun clearProductSession() {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME_PRODUCT, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
