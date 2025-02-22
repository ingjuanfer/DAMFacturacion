package com.example.damfacturacion.views.productos

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.R
import com.example.damfacturacion.SessionManager
import com.example.damfacturacion.controller.ProductoController
import com.example.damfacturacion.model.Producto

class NuevoProductoActivity : AppCompatActivity() {

    private lateinit var editTextCodProducto: EditText
    private lateinit var editTextNombreProducto: EditText
    //private lateinit var switchActivo: Switch
    private lateinit var spinnerUnidadMedida: Spinner
    private lateinit var editTextIVA: EditText
    private lateinit var editTextPrecio: EditText
    private lateinit var editTextLinkImagen: EditText
    private lateinit var buttonAgregarProducto: Button
    private val productoController = ProductoController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_producto)

        // Vincular vistas
        editTextCodProducto = findViewById(R.id.editTextCodProducto)
        editTextNombreProducto = findViewById(R.id.editTextNombreProducto)
        //switchActivo = findViewById(R.id.switchActivo)
        spinnerUnidadMedida = findViewById(R.id.spinnerUnidadMedida)
        editTextIVA = findViewById(R.id.editTextIVA)
        editTextPrecio = findViewById(R.id.editTextPrecio)
        editTextLinkImagen = findViewById(R.id.editTextLinkImagen)
        buttonAgregarProducto = findViewById(R.id.buttonAgregarProducto)

        // Configurar Spinner
        val unidades = arrayOf("Unidad", "Kilogramo", "Metros", "Millar", "Gramos", "Galones", "Litros", "CMCubicos", "Toneladas")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, unidades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUnidadMedida.adapter = adapter

        // Evento de botón
        buttonAgregarProducto.setOnClickListener {
            agregarProducto()
        }
    }

    private fun agregarProducto() {
        val codProducto = editTextCodProducto.text.toString().trim()
        val nombreProducto = editTextNombreProducto.text.toString().trim()
        val activo = true //switchActivo.isChecked
        val unidadMedida = spinnerUnidadMedida.selectedItem.toString()
        val iva = editTextIVA.text.toString().toDoubleOrNull() ?: 0.0
        val precio = editTextPrecio.text.toString().toDoubleOrNull() ?: 0.0
        val linkImage = editTextLinkImagen.text.toString().trim()

        if (codProducto.isEmpty() || nombreProducto.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val producto = Producto(codProducto, nombreProducto, activo, unidadMedida, iva, precio, linkImage)
        val token = SessionManager.encryptedEmpresa

        productoController.agregarProducto(token.toString(), producto) { success, message ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Producto agregado con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
