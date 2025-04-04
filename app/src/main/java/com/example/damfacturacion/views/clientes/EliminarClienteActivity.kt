package com.example.damfacturacion.views.clientes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.R
import com.example.damfacturacion.SessionManager
import com.example.damfacturacion.controller.ClienteController
import com.example.damfacturacion.controller.ProductoController
import com.example.damfacturacion.model.Cliente
import com.example.damfacturacion.model.Producto
import com.example.damfacturacion.views.productos.MenuProductosActivity

class EliminarClienteActivity : AppCompatActivity() {

    private lateinit var editTextBuscarNIF: EditText
    private lateinit var buttonBuscarCliente: Button
    private lateinit var textNIF: TextView
    private lateinit var textNombreCliente: TextView
    private lateinit var textTelefono: TextView
    private lateinit var textEmail: TextView
    private lateinit var textActivo: TextView
    private lateinit var buttonEliminarCliente: Button

    private val clienteController = ClienteController()
    private var clienteActual: Cliente? = null // Guardará el cliente encontrado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_cliente)

        // Vincular vistas con los IDs correctos del XML
        editTextBuscarNIF = findViewById(R.id.editTextBuscarNIF)
        buttonBuscarCliente = findViewById(R.id.buttonBuscarCliente)
        textNIF = findViewById(R.id.textNIF)
        textNombreCliente = findViewById(R.id.textNombreCliente)
        textTelefono = findViewById(R.id.textTelefono)
        textEmail = findViewById(R.id.textEmail)
        textActivo = findViewById(R.id.textActivo)
        buttonEliminarCliente = findViewById(R.id.buttonEliminarCliente)


        // Evento para buscar el producto
        buttonBuscarCliente.setOnClickListener {
            buscarCliente()
        }

        // Evento para eliminar el producto
        buttonEliminarCliente.setOnClickListener {
            eliminarCliente()
        }
    }

    private fun buscarCliente() {
        val nIF = editTextBuscarNIF.text.toString().trim()

        if (nIF.isEmpty()) {
            Toast.makeText(this, "Ingrese un NIF", Toast.LENGTH_SHORT).show()
            return
        }

        val token = SessionManager.encryptedEmpresa

        clienteController.obtenerCliente(token.toString(), nIF) { cliente ,  message ->
            runOnUiThread {
                if (cliente != null) {
                    clienteActual = cliente
                    mostrarInformacionCliente(cliente)
                } else {
                    Toast.makeText(this, "Cliente no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun mostrarInformacionCliente(cliente: Cliente) {
        textNIF.text = cliente.nif
        textNombreCliente.text = cliente.nombreEmpresa
        textTelefono.text = cliente.telefono
        textEmail.text = cliente.email
        textActivo.text = if (cliente.activo == true) "Activo" else "Inactivo"
    }

    private fun eliminarCliente() {
        val cliente = clienteActual ?: run {
            Toast.makeText(this, "No hay cliente seleccionado para eliminar", Toast.LENGTH_SHORT).show()
            return
        }

        val token = SessionManager.encryptedEmpresa

        clienteController.eliminarCliente(token.toString(), cliente.nif) { success, message ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Cliente eliminado con éxito", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                    val intent = Intent(this, MenuClientesActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = if (message.isNotEmpty()) "Error: $message" else "Error desconocido"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun limpiarCampos() {
        editTextBuscarNIF.text.clear()
        textNIF.text = "-"
        textNombreCliente.text = "-"
        textTelefono.text = "-"
        textEmail.text = "-"
        textActivo.text = "-"
        clienteActual = null
    }

    // Método para regresar al menú de productos
    fun menuProductosonClick(view: View) {
        val intent = Intent(this, MenuClientesActivity::class.java)
        startActivity(intent)
        finish()
    }
}
