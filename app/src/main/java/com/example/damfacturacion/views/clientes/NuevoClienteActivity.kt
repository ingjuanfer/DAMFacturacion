package com.example.damfacturacion.views.clientes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.R
import com.example.damfacturacion.SessionManager
import com.example.damfacturacion.controller.ClienteController
import com.example.damfacturacion.model.Cliente
import com.example.damfacturacion.views.clientes.MenuClientesActivity
import com.google.gson.Gson


class NuevoClienteActivity : AppCompatActivity() {

    private lateinit var editTextnif: EditText
    private lateinit var editTexttipoIdentificacion: EditText
    //private lateinit var switchActivo: Switch
    private lateinit var editTextdigitoControl: EditText
    private lateinit var editTextnombreEmpresa: EditText
    private lateinit var editTexttelefono: EditText
    private lateinit var editTextmovil: EditText
    private lateinit var editTextdireccion: EditText
    private lateinit var editTextcodigoPostal: EditText
    private lateinit var editTextemail: EditText
    private lateinit var editTextcodCiudad: EditText
    private lateinit var editTextcodProvincia: EditText
    private lateinit var editTextcodPais: EditText
    private lateinit var buttonAgregarCliente: Button
    private val clienteController = ClienteController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_cliente)

        // Vincular vistas
        editTextnif = findViewById(R.id.editTextnif)
        editTexttipoIdentificacion = findViewById(R.id.editTexttipoIdentificacion)
        //switchActivo = findViewById(R.id.switchActivo)
        editTextdigitoControl = findViewById(R.id.editTextdigitoControl)
        editTextnombreEmpresa = findViewById(R.id.editTextnombreEmpresa)
        editTexttelefono = findViewById(R.id.editTexttelefono)
        editTextmovil = findViewById(R.id.editTextmovil)
        editTextdireccion = findViewById(R.id.editTextdireccion)
        editTextcodigoPostal = findViewById(R.id.editTextcodigoPostal)
        editTextemail = findViewById(R.id.editTextemail)
        editTextcodCiudad = findViewById(R.id.editTextcodCiudad)
        editTextcodProvincia = findViewById(R.id.editTextcodProvincia)
        editTextcodPais = findViewById(R.id.editTextcodPais)
        buttonAgregarCliente = findViewById(R.id.buttonAgregarCliente)

        // Evento de botón
        buttonAgregarCliente.setOnClickListener {
            agregarCliente()
        }
    }

    private fun agregarCliente() {
        val nif = editTextnif.text.toString().trim()
        val tipoIdentificacion = editTexttipoIdentificacion.text.toString().trim()
        val digitoControl = editTextdigitoControl.text.toString().trim()
        val activo = true
        val nombreEmpresa = editTextnombreEmpresa.text.toString().trim()
        val telefono = editTexttelefono.text.toString().trim()
        val movil = editTextmovil.text.toString().trim()
        val direccion = editTextdireccion.text.toString().trim()
        val codigoPostal = editTextcodigoPostal.text.toString().trim()
        val email = editTextemail.text.toString().trim()
        val codCiudad = editTextcodCiudad.text.toString().trim()
        val codProvincia = editTextcodProvincia.text.toString().trim()
        val codPais = editTextcodPais.text.toString().trim()

        if (nif.isEmpty() || nombreEmpresa.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val cliente = Cliente(nif, tipoIdentificacion, digitoControl, activo ,nombreEmpresa, telefono, movil, direccion, codigoPostal, email, codCiudad, codProvincia, codPais)
        val token = SessionManager.encryptedEmpresa

            // Mostrar el JSON antes de enviarlo (para depuración)
        val jsonCliente = Gson().toJson(cliente)
        Log.d("ClienteService", "JSON enviado: $jsonCliente")

        clienteController.agregarCliente(token.toString(), cliente) { success, message ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Cliente agregado con éxito", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MenuClientesActivity::class.java)
                    // Iniciar la actividad MenuClientesActivity
                    startActivity(intent)
                    // Finalizar la actividad para que el usuario no pueda regresar
                    finish()
                } else {
                    val errorMessage = if (message.isNotEmpty()) "Error: $message" else "Error desconocido"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Método para el botón que es el logo de la app para regresar al menu Clientes
    fun menuClientesonClick(view: View) {
        // Crear un Intent para ir a la pantalla del menu clientes
        val intent = Intent(this, MenuClientesActivity::class.java)

        // Iniciar la actividad MenuClientesActivity
        startActivity(intent)

        // Finalizar la actividad de login para que el usuario no pueda regresar
        finish()
    }
}
