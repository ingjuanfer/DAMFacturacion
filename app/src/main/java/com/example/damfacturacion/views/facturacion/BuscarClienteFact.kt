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
import com.example.damfacturacion.controller.ClienteController
import com.example.damfacturacion.model.Cliente
import com.example.damfacturacion.MenuPrincipalActivity
import com.example.damfacturacion.controller.SessionController
import com.example.damfacturacion.views.clientes.MenuClientesActivity
import com.google.gson.Gson

class BuscarClienteFact : AppCompatActivity() {

    private lateinit var editTextBuscarCliente: EditText
    private lateinit var buttonBuscarCliente: Button
    private lateinit var recyclerViewClientes: RecyclerView
    private lateinit var clienteAdapter: ClienteAdapter

    private val clienteController = ClienteController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_cliente_fact)

        // Vincular vistas con los IDs correctos del XML
        editTextBuscarCliente = findViewById(R.id.editTextBuscarCliente)
        buttonBuscarCliente = findViewById(R.id.buttonBuscarCliente)
        recyclerViewClientes = findViewById(R.id.recyclerViewClientes)

        // Configurar el RecyclerView
        clienteAdapter = ClienteAdapter(emptyList(), this) { cliente ->
            seleccionarCliente(cliente)
        }
        recyclerViewClientes.adapter = clienteAdapter
        recyclerViewClientes.layoutManager = LinearLayoutManager(this)

        // Evento para buscar clientes
        buttonBuscarCliente.setOnClickListener {
            buscarClientes()
        }
    }

    private fun buscarClientes() {
        val palabraClave = editTextBuscarCliente.text.toString().trim()

        if (palabraClave.isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre o palabra clave", Toast.LENGTH_SHORT).show()
            return
        }

        val token = SessionManager.encryptedEmpresa
        clienteController.obtenerClientesPorNombre(token.toString(), palabraClave) { clientes, mensaje ->
            runOnUiThread {
                if (clientes != null) {
                    clienteAdapter.actualizarClientes(clientes)
                    Toast.makeText(this, "Clientes cargados: ${clientes.size}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun seleccionarCliente(cliente: Cliente) {
        // Guardar la información del cliente en la sesión
        val sessionController = SessionController(this)
        sessionController.saveClientSession(cliente)

        // Continuar a la siguiente pantalla
        val intent = Intent(this, FormularioProductos::class.java)
        startActivity(intent)
        finish()
    }


    fun menuClientesOnClick(view: View) {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        startActivity(intent)
        finish()
    }
}
