package com.example.damfacturacion.views.productos

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.damfacturacion.R
import com.example.damfacturacion.adapter.ProductoAdapter
import com.example.damfacturacion.api.RetrofitClient
import com.example.damfacturacion.controller.SessionController
import com.example.damfacturacion.interfaces.ProductoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InformeProductosActivity : AppCompatActivity() {

    private lateinit var textViewBienvenido: TextView
    private lateinit var textViewNombreUsuario: TextView
    private lateinit var productoService: ProductoService
    private lateinit var recyclerViewProductos: RecyclerView // Referencia al RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informe_productos)

        // Inicializar las vistas
        textViewBienvenido = findViewById(R.id.textViewBienvenido)
        textViewNombreUsuario = findViewById(R.id.textViewNombreUsuario)
        recyclerViewProductos = findViewById(R.id.recyclerViewProductos) // Asignar RecyclerView

        productoService = RetrofitClient.getProductoService()

        val sessionController = SessionController(this)
        val usuario = sessionController.getSession()

        if (usuario != null) {
            // Mostrar el nombre del usuario y otros detalles
            textViewBienvenido.text = "INFORME DE PRODUCTOS"
            textViewNombreUsuario.text = "${usuario.nombre}"
        } else {
            // Manejar el caso cuando no hay sesión
        }

        val adapter = ProductoAdapter(emptyList())
        recyclerViewProductos.adapter = adapter // Asignar el adapter al RecyclerView

        // Lanzar la corrutina para obtener los productos
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val productos = productoService.getProductos()
                withContext(Dispatchers.Main) {
                    adapter.submitList(productos)
                }
            } catch (e: Exception) {
                // Manejar el error, por ejemplo, mostrar un mensaje al usuario
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@InformeProductosActivity, "Error al obtener los productos: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
