package com.example.damfacturacion.views.facturacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.damfacturacion.R
import com.example.damfacturacion.SessionManager
import com.example.damfacturacion.controller.ProductoController
import com.example.damfacturacion.controller.SessionController
import android.util.Log
import com.example.damfacturacion.model.ListaProductosBuscados

class BuscarProductos : AppCompatActivity() {

    private lateinit var editTextBuscarProducto: EditText
    private lateinit var buttonBuscarProducto: Button
    private lateinit var recyclerViewProductos: RecyclerView
    private lateinit var productoAdapter: BuscadorProductosAdapter

    private val productoController = ProductoController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_producto_fact)

        // Vincular vistas con los IDs correctos del XML
        editTextBuscarProducto = findViewById(R.id.editTextBuscarProducto)
        buttonBuscarProducto = findViewById(R.id.buttonBuscarProducto)
        recyclerViewProductos = findViewById(R.id.recyclerViewProductos)

        // Configurar el RecyclerView
        productoAdapter = BuscadorProductosAdapter(emptyList(), this) { producto ->
            seleccionarProducto(producto)
        }
        recyclerViewProductos.adapter = productoAdapter
        recyclerViewProductos.layoutManager = LinearLayoutManager(this)

        // Evento para buscar productos
        buttonBuscarProducto.setOnClickListener {
            buscarProductos()
        }
    }

    private fun buscarProductos() {
        try {
            val palabraClave = editTextBuscarProducto.text.toString().trim()

            if (palabraClave.isEmpty()) {
                Toast.makeText(this, "Ingrese un nombre o palabra clave", Toast.LENGTH_SHORT).show()
                return
            }

            val token = SessionManager.encryptedEmpresa
            productoController.obtenerProductosPorNombre(
                token.toString(),
                palabraClave
            ) { productos, mensaje ->
                runOnUiThread {
                    try {
                        if (productos != null) {
                            productoAdapter.actualizarProductos(productos)
                            Toast.makeText(
                                this,
                                "Productos cargados: ${productos.size}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e: Exception) {
                        Log.e("BuscarProductos", "Error al cargar productos", e)
                        Toast.makeText(this, "Error inesperado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("BuscarProductos", "Error al buscar productos", e)
            Toast.makeText(this, "Error inesperado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun seleccionarProducto(producto: ListaProductosBuscados) {
        // Guardar la información del producto en la sesión
        val sessionController = SessionController(this)
        sessionController.saveProductSession(producto)

        // Continuar a la siguiente pantalla
        val intent = Intent(this, FormularioProductos::class.java)
        startActivity(intent)
        finish()
    }


    // Método para el botón que es el logo de la app para regresar al formulario productos
    fun regresaronClick(view: View) {
        val intent = Intent(this, FormularioProductos  ::class.java)
        startActivity(intent)
        finish()
    }

}
