package com.example.damfacturacion.views.productos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.R
import com.example.damfacturacion.SessionManager
import com.example.damfacturacion.controller.ProductoController
import com.example.damfacturacion.model.Producto
import com.google.gson.Gson

class EditarProductoActivity : AppCompatActivity() {

    private lateinit var editTextBuscarCodProducto: EditText
    private lateinit var buttonBuscarProducto: Button
    private lateinit var textNIF: TextView
    private lateinit var editNombreProducto: EditText
    private lateinit var editIVA: EditText
    private lateinit var editPrecio: EditText
    private lateinit var editLinkImagen: EditText
    private lateinit var spinnerUnidadMedida: Spinner
    private lateinit var buttonGuardarProducto: Button
   // private lateinit var switchActivo: Switch

    private val productoController = ProductoController()
    private var productoActual: Producto? = null // Guardará el producto encontrado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        // Vincular vistas con los IDs correctos del XML
        editTextBuscarCodProducto = findViewById(R.id.editTextBuscarCodProducto)
        buttonBuscarProducto = findViewById(R.id.buttonBuscarProducto)
        textNIF = findViewById(R.id.textCodProducto)
        editNombreProducto = findViewById(R.id.editNombreProducto)
        editIVA = findViewById(R.id.editIVA)
        editPrecio = findViewById(R.id.editPrecio)
        editLinkImagen = findViewById(R.id.editLinkImagen)
        spinnerUnidadMedida = findViewById(R.id.spinnerUnidadMedida)
        //switchActivo = findViewById(R.id.switchActivo)
        buttonGuardarProducto = findViewById(R.id.buttonGuardarProducto)

        // Configurar Spinner
        val unidades = arrayOf("Unidad", "Kilogramo", "Metros", "Millar", "Gramos", "Galones", "Litros", "CMCubicos", "Toneladas")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, unidades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUnidadMedida.adapter = adapter

        // Evento para buscar el producto
        buttonBuscarProducto.setOnClickListener {
            buscarProducto()
        }

        // Evento para guardar los cambios del producto
        buttonGuardarProducto.setOnClickListener {
            guardarCambiosProducto()
        }
    }

    private fun buscarProducto() {
        val codProducto = editTextBuscarCodProducto.text.toString().trim()
        if (codProducto.isEmpty()) {
            Toast.makeText(this, "Ingrese un código de producto", Toast.LENGTH_SHORT).show()
            return
        }
        val token = SessionManager.encryptedEmpresa
        productoController.obtenerProducto(token.toString(), codProducto) { producto, message ->
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
        textNIF.text = editTextBuscarCodProducto.text.toString()
        editNombreProducto.setText(producto.nombreProducto)
        spinnerUnidadMedida.setSelection(0) // Establecer la posición del spinner en "Unidad"setText(producto.unidadMedida)
        editIVA.setText(producto.iva.toString())
        editPrecio.setText(producto.precio.toString())
        editLinkImagen.setText(producto.linkImage.toString())
        //switchActivo.isChecked = producto.activo == true

        // Seleccionar el estado en el spinner
        val index = (spinnerUnidadMedida.adapter as ArrayAdapter<String>).getPosition(producto.unidadMedida)
        if (index >= 0) spinnerUnidadMedida.setSelection(index)
    }

    private fun guardarCambiosProducto() {
        val producto = productoActual ?: run {
            Toast.makeText(this, "No hay producto seleccionado para editar", Toast.LENGTH_SHORT).show()
            return
        }

        val token = SessionManager.encryptedEmpresa

        val productoEditado = Producto(
            codProducto = producto.codProducto,
            nombreProducto = editNombreProducto.text.toString().trim(),
            unidadMedida = spinnerUnidadMedida.selectedItem.toString(),
            iva = editIVA.text.toString().toDoubleOrNull() ?: 0.0,
            precio = editPrecio.text.toString().toDoubleOrNull() ?: 0.0,
            linkImage = editLinkImagen.text.toString().trim(),
            activo = true // activo = switchActivo.isChecked
        )

        // Mostrar el JSON antes de enviarlo (para depuración)
        val jsonCliente = Gson().toJson(productoEditado)
        Log.d("ProductoService", "JSON enviado: $jsonCliente")

        productoController.actualizarProducto(token.toString(), productoEditado) { success, message ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Producto actualizado con éxito", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MenuProductosActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = if (message.isNotEmpty()) "Error: $message" else "Error desconocido"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Método para regresar al menú de productos
    fun menuProductosonClick(view: View) {
        val intent = Intent(this, MenuProductosActivity::class.java)
        startActivity(intent)
        finish()
    }
}
