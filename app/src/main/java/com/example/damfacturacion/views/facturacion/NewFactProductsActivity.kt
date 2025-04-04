package com.example.damfacturacion.views.facturacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.MenuPrincipalActivity
import com.example.damfacturacion.R
import com.example.damfacturacion.SessionManager
import com.example.damfacturacion.controller.ProductoController
import com.example.damfacturacion.model.Producto
import com.example.damfacturacion.views.productos.MenuProductosActivity

class NewFactProductsActivity : AppCompatActivity() {

    private lateinit var editTextBuscarCodProducto: EditText
    private lateinit var buttonBuscarProducto: Button
    private lateinit var textCodProducto: TextView
    private lateinit var textNombreProducto: TextView
    private lateinit var textUnidadMedida: TextView
    private lateinit var textIVA: TextView
    private lateinit var textPrecio: TextView
    private lateinit var buttonGuardarFactura: Button

    private val productoController = ProductoController()
    private var productoActual: Producto? = null // Guardará el producto encontrado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_fact_products)

        // Vincular vistas con los IDs correctos del XML
        editTextBuscarCodProducto = findViewById(R.id.editTextBuscarCodProducto)
        buttonBuscarProducto = findViewById(R.id.buttonBuscarProducto)
        textCodProducto = findViewById(R.id.textCodProducto)
        textNombreProducto = findViewById(R.id.textNombreProducto)
        textUnidadMedida = findViewById(R.id.textUnidadMedida)
        textIVA = findViewById(R.id.textIVA)
        textPrecio = findViewById(R.id.textPrecio)
        buttonGuardarFactura = findViewById(R.id.buttonGuardarFactura)

        // Evento para buscar el producto
        buttonBuscarProducto.setOnClickListener {
            buscarProducto()
        }

        // Evento para Guardar Factura
        buttonGuardarFactura.setOnClickListener {
            guardarFactura()
        }
    }

    private fun buscarProducto() {
        val codProducto = editTextBuscarCodProducto.text.toString().trim()

        if (codProducto.isEmpty()) {
            Toast.makeText(this, "Ingrese un código de producto", Toast.LENGTH_SHORT).show()
            return
        }

        val token = SessionManager.encryptedEmpresa

        productoController.obtenerProducto(token.toString(), codProducto) { producto ,  message ->
            runOnUiThread {
                if (producto != null) {
                    productoActual = producto
                    mostrarInformacionProducto(producto)
                } else {
                    Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun mostrarInformacionProducto(producto: Producto) {
        textCodProducto.text = producto.codProducto
        textNombreProducto.text = producto.nombreProducto
        textUnidadMedida.text = producto.unidadMedida
        textIVA.text = producto.iva.toString()
        textPrecio.text = producto.precio.toString()
    }

    private fun guardarFactura() {
        val producto = productoActual ?: run {

        }
/*
        val token = SessionManager.encryptedEmpresa

        productoController.eliminarProducto(token.toString(), producto.codProducto) { success, message ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Producto eliminado con éxito", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                    val intent = Intent(this, MenuProductosActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = if (message.isNotEmpty()) "Error: $message" else "Error desconocido"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
        */
    }

    private fun limpiarCampos() {
        editTextBuscarCodProducto.text.clear()
        textCodProducto.text = "-"
        textNombreProducto.text = "-"
        textUnidadMedida.text = "-"
        textIVA.text = "-"
        textPrecio.text = "-"
        productoActual = null
    }

    // Método para regresar al menú de productos
    fun menuProductosonClick(view: View) {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        startActivity(intent)
        finish()
    }
}
