package com.example.damfacturacion.views.productos

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.R
import com.example.damfacturacion.SessionManager
import com.example.damfacturacion.controller.ProductoController
import com.example.damfacturacion.model.Producto

class EliminarProductoActivity : AppCompatActivity() {

    private lateinit var editTextBuscarCodProducto: EditText
    private lateinit var buttonBuscarProducto: Button
    private lateinit var textCodProducto: TextView
    private lateinit var textNombreProducto: TextView
    private lateinit var textUnidadMedida: TextView
    private lateinit var textIVA: TextView
    private lateinit var textPrecio: TextView
    private lateinit var textLinkImagen: TextView
    private lateinit var textActivo: TextView
    private lateinit var buttonEliminarProducto: Button

    private val productoController = ProductoController()
    private var productoActual: Producto? = null // Guardará el producto encontrado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_producto)

        // Vincular vistas con los IDs correctos del XML
        editTextBuscarCodProducto = findViewById(R.id.editTextBuscarCodProducto)
        buttonBuscarProducto = findViewById(R.id.buttonBuscarProducto)
        textCodProducto = findViewById(R.id.textCodProducto)
        textNombreProducto = findViewById(R.id.textNombreProducto)
        textUnidadMedida = findViewById(R.id.textUnidadMedida)
        textIVA = findViewById(R.id.textIVA)
        textPrecio = findViewById(R.id.textPrecio)
        textLinkImagen = findViewById(R.id.textLinkImagen)
        textActivo = findViewById(R.id.textActivo)
        buttonEliminarProducto = findViewById(R.id.buttonEliminarProducto)

        // Evento para buscar el producto
        buttonBuscarProducto.setOnClickListener {
            buscarProducto()
        }

        // Evento para eliminar el producto
        buttonEliminarProducto.setOnClickListener {
            eliminarProducto()
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
        textLinkImagen.text = producto.linkImage.toString()
        textActivo.text = if (producto.activo == true) "Activo" else "Inactivo"
    }

    private fun eliminarProducto() {
        val producto = productoActual ?: run {
            Toast.makeText(this, "No hay producto seleccionado para eliminar", Toast.LENGTH_SHORT).show()
            return
        }

        val token = SessionManager.encryptedEmpresa

        productoController.eliminarProducto(token.toString(), producto.codProducto) { success, message ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Producto eliminado con éxito", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                } else {
                    val errorMessage = if (message.isNotEmpty()) "Error: $message" else "Error desconocido"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun limpiarCampos() {
        editTextBuscarCodProducto.text.clear()
        textCodProducto.text = "-"
        textNombreProducto.text = "-"
        textUnidadMedida.text = "-"
        textIVA.text = "-"
        textPrecio.text = "-"
        textLinkImagen.text = "-"
        textActivo.text = "-"
        productoActual = null
    }

    // Método para regresar al menú de productos
    fun menuProductosonClick(view: View) {
        val intent = Intent(this, MenuProductosActivity::class.java)
        startActivity(intent)
        finish()
    }
}
