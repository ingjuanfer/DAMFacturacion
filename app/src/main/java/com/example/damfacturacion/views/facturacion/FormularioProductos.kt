package com.example.damfacturacion.views.facturacion

import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.R
import com.example.damfacturacion.controller.SessionController
import com.example.damfacturacion.model.Cliente
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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
import com.example.damfacturacion.model.Usuario
import android.widget.ImageView
import com.example.damfacturacion.model.ListaProductosBuscados
import com.example.damfacturacion.model.Producto
import android.text.Editable
import androidx.core.widget.doOnTextChanged

class FormularioProductos : AppCompatActivity() {

    private lateinit var adapter: ProductoAdapter
    private lateinit var buscadorProductosAdapter: BuscadorProductosAdapter

    private val productosSeleccionados = mutableListOf<ProductoSeleccionado>()
    private val productosBuscados = mutableListOf<ListaProductosBuscados>()

    private lateinit var editTextCodigo: EditText
    private lateinit var editTextCantidad: EditText
    private lateinit var buttonAgregar: Button
    private lateinit var recyclerViewProductos: RecyclerView
    private lateinit var textViewCliente: TextView

    private lateinit var editTextBuscarProducto: EditText
    private lateinit var buttonBuscarProducto: Button
    private lateinit var recyclerViewBusquedaProductos: RecyclerView

    private val productoController = ProductoController()

    private lateinit var cliente: Cliente
    private lateinit var usuario: Usuario
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_productos)

        val buttonVolver = findViewById<Button>(R.id.buttonMenuPrincipal)
        buttonVolver.setOnClickListener {
            menuPrincipalOnClick()
        }

        val buttonBuscarProducto = findViewById<Button>(R.id.buttonBuscarProducto)
        buttonBuscarProducto.setOnClickListener {
            buscarProductos()
        }

        inicializarSesion()
        inicializarViews()
        configurarRecyclerView()
        mostrarCliente()
        configurarEventos()
    }

    private fun menuPrincipalOnClick() {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun actualizarListaProductosSeleccionados() {
        adapter.notifyDataSetChanged()
    }

    private fun inicializarSesion() {
        val sessionController = SessionController(this)
        cliente = sessionController.getClientSession() ?: Cliente(
            nif = "",
            tipoIdentificacion = null,
            digitoControl = null,
            activo = false,
            nombreEmpresa = null,
            telefono = null,
            movil = null,
            direccion = null,
            codigoPostal = null,
            email = null,
            codCiudad = null,
            codProvincia = null,
            codPais = null
        )
        usuario = sessionController.getSession() ?: Usuario(
            dni = "",
            nombre = "",
            userSesion = "",
            claveSesion = "",
            fechaRegistro = "",
            activo = false,
            tel_Movil = "",
            email = ""
        )
        token = SessionManager.encryptedEmpresa ?: ""
    }

    private fun inicializarViews() {
        editTextCodigo = findViewById(R.id.editTextCodigo)
        editTextCantidad = findViewById(R.id.editTextCantidad)
        buttonAgregar = findViewById(R.id.buttonAgregar)
        recyclerViewProductos = findViewById(R.id.recyclerViewProductos)
        textViewCliente = findViewById(R.id.textViewCliente)

        editTextBuscarProducto = findViewById(R.id.editTextBuscarProducto)
        buttonBuscarProducto = findViewById(R.id.buttonBuscarProducto)
        recyclerViewBusquedaProductos = findViewById(R.id.recyclerViewBusquedaProductos)
    }

    private fun configurarRecyclerView() {
        adapter = ProductoAdapter(productosSeleccionados)
        recyclerViewProductos.layoutManager = LinearLayoutManager(this)
        recyclerViewProductos.adapter = adapter

        // Configurar el RecyclerView
        buscadorProductosAdapter = BuscadorProductosAdapter(emptyList(), this) { producto ->
            seleccionarProducto(producto)
        }
        recyclerViewBusquedaProductos.adapter = buscadorProductosAdapter
        recyclerViewBusquedaProductos.layoutManager = LinearLayoutManager(this)
    }

    private fun seleccionarProducto(producto: ListaProductosBuscados) {
        // Guardar la información del producto en la sesión
        val sessionController = SessionController(this)
        sessionController.saveProductSession(producto)

        // Rellenar automáticamente el campo de código
        editTextCodigo.setText(producto.codProducto)

        // Opcional: puedes mover el foco al campo cantidad para facilitar la entrada
        editTextCantidad.requestFocus()

        // También puedes cerrar el RecyclerView de búsqueda si quieres
        // recyclerViewBusquedaProductos.visibility = View.GONE
    }

    private fun mostrarCliente() {
        if (!cliente.nombreEmpresa.isNullOrEmpty()) {
            textViewCliente.text = "Cliente seleccionado: ${cliente.nombreEmpresa} - ${cliente.nif}"
        } else {
            textViewCliente.text = "No hay cliente seleccionado"
        }
    }

    private fun configurarEventos() {
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
            guardarFactura()
        }

        findViewById<Button>(R.id.buttonMenuPrincipal)?.setOnClickListener {
            menuPrincipalonClick()
        }

        editTextBuscarProducto.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) {
                buscadorProductosAdapter.limpiarProductos()
            }
        }


    }

    private fun obtenerProductoYAgregar(codigo: String, cantidad: Int) {
        val productoController = ProductoController()

        productoController.obtenerProducto(token, codigo) { producto, mensaje ->
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
                    editTextCodigo.text.clear()
                    editTextCantidad.text.clear()
                } else {
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun guardarFactura() {
        if (cliente.nif.isNotEmpty() && usuario.dni.isNotEmpty() && productosSeleccionados.isNotEmpty()) {
            val encabezado = FacturaEncabezado(
                NIF = cliente.nif,
                DNI_Usuario_Reg = usuario.dni
            )

            val facturaController = FacturaController()
            facturaController.crearEncabezadoFactura(token, encabezado) { nroDcto, mensaje ->
                runOnUiThread {
                    if (nroDcto != null) {
                        guardarDetallesFactura(nroDcto.toString(), facturaController)
                    } else {
                        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "No se puede guardar la factura. Verifique cliente y productos.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarDetallesFactura(nroDcto: String, facturaController: FacturaController) {
        var detallesGuardados = 0

        productosSeleccionados.forEach { producto ->
            val detalle = FacturaDetalle(
                nroDcto = nroDcto.toInt(),
                codProducto = producto.codProducto,
                cantidad = producto.cantidad.toDouble(),
                precio_Unitario = producto.precio,
                iva = producto.iva
            )

            facturaController.crearDetalleFactura(token, detalle) { success, mensaje ->
                if (success) {
                    detallesGuardados++
                    if (detallesGuardados == productosSeleccionados.size) {
                        generarFacturaPOS(facturaController, nroDcto)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun generarFacturaPOS(facturaController: FacturaController, nroDcto: String) {
        facturaController.generarFacturaPOS(token, nroDcto.toInt()) { success, mensaje ->
            runOnUiThread {
                if (success) {
                    AlertDialog.Builder(this)
                        .setTitle("Factura generada")
                        .setMessage("Factura guardada y generada correctamente.")
                        .setPositiveButton("OK") { dialog, _ ->
                            productosSeleccionados.clear()
                            adapter.notifyDataSetChanged()
                            dialog.dismiss()
                            startActivity(Intent(this, MenuPrincipalActivity::class.java))
                            finish()
                        }
                        .setCancelable(false)
                        .show()
                } else {
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun menuPrincipalonClick() {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun buscarProductos() {
        try {
            val palabraClave = editTextBuscarProducto.text.toString().trim()

            if (palabraClave.isEmpty()) {
                Toast.makeText(this, "Ingrese un nombre o palabra clave", Toast.LENGTH_SHORT).show()
                return
            }

            val token = SessionManager.encryptedEmpresa
            productoController.obtenerProductosPorNombre(
                token.toString(),
                palabraClave
            ) { productos, mensaje ->
                runOnUiThread {
                    try {
                        if (productos != null) {
                            buscadorProductosAdapter.actualizarProductos(productos)
                            Toast.makeText(
                                this,
                                "Productos cargados: ${productos.size}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("BuscarProductos", "Error al cargar productos", e)
                        Toast.makeText(this, "Error inesperado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("BuscarProductos", "Error al buscar productos", e)
            Toast.makeText(this, "Error inesperado", Toast.LENGTH_SHORT).show()
        }
    }



}