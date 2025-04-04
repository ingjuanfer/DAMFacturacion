package com.example.damfacturacion.views.productos

import com.example.damfacturacion.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.MenuPrincipalActivity
import com.example.damfacturacion.SessionManager
import com.example.damfacturacion.controller.SessionController

class MenuProductosActivity : AppCompatActivity() {

    private lateinit var textViewBienvenido: TextView
    private lateinit var textViewNombreUsuario: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_producto)

        // Inicializar las vistas
        textViewBienvenido = findViewById(R.id.textViewBienvenido)
        textViewNombreUsuario = findViewById(R.id.textViewNombreUsuario)

        val sessionController = SessionController(this)
        val usuario = sessionController.getSession()

        if (usuario != null) {
            // Mostrar el nombre del usuario y otros detalles
            textViewBienvenido.text = "PRODUCTOS"
            textViewNombreUsuario.text = "${usuario.nombre}"
        } else {
            // Manejar el caso cuando no hay sesión
        }
        val encryptedEmpresa = SessionManager.encryptedEmpresa
        if (encryptedEmpresa != null) {
            println("Empresa Encrypted: $encryptedEmpresa")
        }

    }

    // Método para el botón que es el logo de la app para regresar al menu Principal
    fun menuPrincipalonClick(view: View) {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Método para el botón Nuevo Producto
    fun nuevoProductoonClick(view: View) {
        // Crear un Intent para ir a la pantalla principal
        val intent = Intent(this, NuevoProductoActivity::class.java)

        // Iniciar la actividad NuevoProductoActivity
        startActivity(intent)

        // Finalizar la actividad de login para que el usuario no pueda regresar
        finish()
    }

    // Método para el botón Editar Producto
    fun editarProductoonClick(view: View) {
        // Crear un Intent para ir a la pantalla principal
        val intent = Intent(this, EditarProductoActivity::class.java)

        // Iniciar la actividad NuevoProductoActivity
        startActivity(intent)

        // Finalizar la actividad de login para que el usuario no pueda regresar
        finish()
    }

    // Método para el botón Eliminar Producto
    fun eliminarProductoonClick(view: View) {
        val intent = Intent(this, EliminarProductoActivity::class.java)
        startActivity(intent)
        finish()
    }



}
