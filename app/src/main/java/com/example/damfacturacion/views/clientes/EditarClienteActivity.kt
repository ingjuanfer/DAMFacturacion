package com.example.damfacturacion.views.clientes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.R
import com.example.damfacturacion.SessionManager
import com.example.damfacturacion.controller.ClienteController
import com.example.damfacturacion.model.Cliente
import com.google.gson.Gson

class EditarClienteActivity : AppCompatActivity() {

    private lateinit var editTextBuscarNIF: EditText
    private lateinit var buttonBuscarCliente: Button
    private lateinit var textNIF: TextView
    private lateinit var editTextnombreEmpresa: EditText
    private lateinit var editTexttelefono: EditText
    private lateinit var editTextemail: EditText
    private lateinit var editTextdireccion: EditText
    private lateinit var editTextdigitoControl: EditText
    private lateinit var editTextmovil: EditText
    private lateinit var editTextcodigoPostal: EditText
    private lateinit var editTextcodCiudad: EditText
    private lateinit var editTextcodProvincia: EditText
    private lateinit var editTextcodPais: EditText
    private lateinit var buttonGuardarCliente: Button
    private val clienteController = ClienteController()
    private var clienteActual: Cliente? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cliente)

        // Vincular vistas con los IDs correctos del XML
        editTextBuscarNIF = findViewById(R.id.editTextBuscarNIF)
        buttonBuscarCliente = findViewById(R.id.buttonBuscarCliente)
        textNIF = findViewById(R.id.textNIF)
        editTextnombreEmpresa = findViewById(R.id.editTextnombreEmpresa)
        editTexttelefono = findViewById(R.id.editTexttelefono)
        editTextemail = findViewById(R.id.editTextemail)
        editTextdireccion = findViewById(R.id.editTextdireccion)
        editTextdigitoControl = findViewById(R.id.editTextdigitoControl)
        editTextmovil = findViewById(R.id.editTextmovil)
        editTextcodigoPostal = findViewById(R.id.editTextcodigoPostal)
        editTextcodCiudad = findViewById(R.id.editTextcodCiudad)
        editTextcodProvincia = findViewById(R.id.editTextcodProvincia)
        editTextcodPais = findViewById(R.id.editTextcodPais)
        //switchActivo = findViewById(R.id.switchActivo)

        buttonGuardarCliente = findViewById(R.id.buttonGuardarCliente)

        buttonBuscarCliente.setOnClickListener {
            buscarCliente()
        }

        buttonGuardarCliente.setOnClickListener {
            guardarCambiosCliente()
        }
    }

    private fun buscarCliente() {
        val nif = editTextBuscarNIF.text.toString().trim()
        if (nif.isEmpty()) {
            Toast.makeText(this, "Ingrese un NIF", Toast.LENGTH_SHORT).show()
            return
        }
        val token = SessionManager.encryptedEmpresa
        clienteController.obtenerCliente(token.toString(), nif) { cliente, message ->
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
        textNIF.text = editTextBuscarNIF.text.toString()
        editTextnombreEmpresa.setText(cliente.nombreEmpresa)
        editTexttelefono.setText(cliente.telefono)
        editTextemail.setText(cliente.email)
        editTextdireccion.setText(cliente.direccion)
        editTextmovil.setText(cliente.movil)
        editTextcodigoPostal.setText(cliente.codigoPostal)
        editTextcodCiudad.setText(cliente.codCiudad)
        editTextcodProvincia.setText(cliente.codProvincia)
        editTextcodPais.setText(cliente.codPais)
        editTextdigitoControl.setText(cliente.digitoControl)
        val activo = true  //if (cliente.activo) "Activo" else "Inactivo"
    }

    private fun guardarCambiosCliente() {
        val cliente = clienteActual ?: run {
            Toast.makeText(this, "No hay cliente seleccionado para editar", Toast.LENGTH_SHORT).show()
            return
        }

        val token = SessionManager.encryptedEmpresa

        val clienteEditado = Cliente(
            nif = cliente.nif,
            nombreEmpresa = editTextnombreEmpresa.text.toString().trim(),
            telefono = editTexttelefono.text.toString().trim(),
            email = editTextemail.text.toString().trim(),
            activo = true,  // activo = switchActivo.isChecked
            direccion = editTextdireccion.text.toString().trim(),
            tipoIdentificacion = "NIF",
            digitoControl = editTextdigitoControl.text.toString().trim(),
            movil = editTextmovil.text.toString().trim(),
            codigoPostal = editTextcodigoPostal.text.toString().trim(),
            codCiudad = editTextcodCiudad.text.toString().trim(),
            codProvincia = editTextcodProvincia.text.toString().trim(),
            codPais = editTextcodPais.text.toString().trim()
        )

        val jsonCliente = Gson().toJson(clienteEditado)
        Log.d("ClienteService", "JSON enviado: $jsonCliente")

        clienteController.actualizarCliente(token.toString(), clienteEditado) { success, message ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Cliente actualizado con éxito", Toast.LENGTH_SHORT).show()
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

    fun menuClientesOnClick(view: View) {
        val intent = Intent(this, MenuClientesActivity::class.java)
        startActivity(intent)
        finish()
    }
}
