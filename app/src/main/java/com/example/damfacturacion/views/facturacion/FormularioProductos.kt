package com.example.damfacturacion.views.facturacion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.R
import com.example.damfacturacion.controller.SessionController
import com.example.damfacturacion.model.Cliente
import android.widget.TextView

class FormularioProductos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_productos)

        val sessionController = SessionController(this)
        val cliente = sessionController.getClientSession()

        if (cliente != null) {
            // Mostrar el cliente seleccionado
            val textViewCliente = findViewById<TextView>(R.id.textViewCliente)
            textViewCliente.text = "Cliente seleccionado: ${cliente.nombreEmpresa} - ${cliente.nif}"
        } else {
            // Manejar el caso cuando no hay sesión
            println("No hay cliente seleccionado")
        }
    }
}
