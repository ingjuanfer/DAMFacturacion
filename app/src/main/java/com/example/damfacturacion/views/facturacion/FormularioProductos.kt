package com.example.damfacturacion.views.facturacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.R
import com.example.damfacturacion.controller.SessionController
import com.example.damfacturacion.model.Cliente
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.damfacturacion.MenuPrincipalActivity
import com.example.damfacturacion.SessionManager
import com.example.damfacturacion.views.facturacion.ProductoAdapter
import com.example.damfacturacion.controller.ProductoController
import com.example.damfacturacion.model.ProductoSeleccionado
import com.example.damfacturacion.controller.FacturaController
import com.example.damfacturacion.model.FacturaEncabezado
import com.example.damfacturacion.model.FacturaDetalle


class FormularioProductos : AppCompatActivity() {

    private lateinit var adapter: ProductoAdapter
    private val productosSeleccionados = mutableListOf<ProductoSeleccionado>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_productos)

        val sessionController = SessionController(this)
        val cliente = sessionController.getClientSession()
        val usuario = sessionController.getSession()
        val editTextCodigo = findViewById<EditText>(R.id.editTextCodigo)
        val editTextCantidad = findViewById<EditText>(R.id.editTextCantidad)
        val buttonAgregar = findViewById<Button>(R.id.buttonAgregar)
        val recyclerViewProductos = findViewById<RecyclerView>(R.id.recyclerViewClientes)

        adapter = ProductoAdapter(productosSeleccionados)
        recyclerViewProductos.layoutManager = LinearLayoutManager(this)
        recyclerViewProductos.adapter = adapter

        if (cliente != null) {
            val textViewCliente = findViewById<TextView>(R.id.textViewCliente)
            textViewCliente.text = "Cliente seleccionado: ${cliente.nombreEmpresa} - ${cliente.nif}"
        } else {
            println("No hay cliente seleccionado")
        }

        buttonAgregar.setOnClickListener {
            val codigo = editTextCodigo.text.toString().trim()
            val cantidad = editTextCantidad.text.toString().trim().toIntOrNull()

            if (codigo.isNotEmpty() && cantidad != null && cantidad > 0) {
                obtenerProductoYAgregar(codigo, cantidad)
            } else {
                Toast.makeText(this, "Ingrese un código y cantidad válida", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.buttonGuardarFactura).setOnClickListener {
            if (cliente != null && usuario != null && productosSeleccionados.isNotEmpty()) {
                val encabezado = FacturaEncabezado(
                    NIF = cliente.nif,
                    DNI_Usuario_Reg = usuario.dni
                )
                val token = SessionManager.encryptedEmpresa
                val facturaController = FacturaController()
                facturaController.crearEncabezadoFactura(token.toString(), encabezado) { nroDcto, mensaje ->
                    runOnUiThread {
                        if (nroDcto != null) {
                            var detallesGuardados = 0
                            productosSeleccionados.forEach { producto ->
                                val detalle = FacturaDetalle(
                                    nroDcto = nroDcto,
                                    codProducto = producto.codProducto,
                                    cantidad = producto.cantidad.toDouble(),
                                    precio_Unitario = producto.precio,
                                    iva = producto.iva
                                )
                                facturaController.crearDetalleFactura(token.toString(), detalle) { success, detalleMensaje ->
                                    if (success) {
                                        detallesGuardados++
                                        if (detallesGuardados == productosSeleccionados.size) {
                                            facturaController.generarFacturaPOS(token.toString(), nroDcto) { posSuccess, posMensaje ->
                                                runOnUiThread {
                                                    if (posSuccess) {
                                                        AlertDialog.Builder(this)
                                                            .setTitle("Factura generada")
                                                            .setMessage("Factura guardada y generada correctamente.")
                                                            .setPositiveButton("OK") { dialog, _ ->
                                                                productosSeleccionados.clear()
                                                                adapter.notifyDataSetChanged()
                                                                dialog.dismiss()
                                                                val intent = Intent(this, MenuPrincipalActivity::class.java)
                                                                startActivity(intent)
                                                                finish()
                                                            }
                                                            .setCancelable(false)
                                                            .show()
                                                    } else {
                                                        Toast.makeText(this, posMensaje, Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(this, detalleMensaje, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "No se puede guardar la factura. Verifique que haya cliente y productos seleccionados.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerProductoYAgregar(codigo: String, cantidad: Int) {
        val token = SessionManager.encryptedEmpresa
        val productoController = ProductoController()

        productoController.obtenerProducto(token.toString(), codigo) { producto, mensaje ->
            runOnUiThread {
                if (producto != null) {
                    val productoSeleccionado = ProductoSeleccionado(
                        codProducto = producto.codProducto,
                        nombreProducto = producto.nombreProducto ?: "Sin nombre",
                        precio = producto.precio ?: 0.0,
                        cantidad = cantidad,
                        iva = producto.iva ?: 0.0
                    )
                    adapter.agregarProducto(productoSeleccionado)
                    findViewById<EditText>(R.id.editTextCodigo).text.clear()
                    findViewById<EditText>(R.id.editTextCantidad).text.clear()
                } else {
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Método para el botón que es el logo de la app para regresar al menu Principal
    fun menuPrincipalonClick(view: View) {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        startActivity(intent)
        finish()
    }


}
