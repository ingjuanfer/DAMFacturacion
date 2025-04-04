package com.example.damfacturacion.views.clientes

import com.example.damfacturacion.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.MenuPrincipalActivity
import com.example.damfacturacion.SessionManager
import com.example.damfacturacion.controller.SessionController
import com.example.damfacturacion.views.clientes.NuevoClienteActivity

class MenuClientesActivity  : AppCompatActivity() {

    private lateinit var textViewBienvenido: TextView
    private lateinit var textViewNombreUsuario: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_cliente)

        // Inicializar las vistas
        textViewBienvenido = findViewById(R.id.textViewBienvenido)
        textViewNombreUsuario = findViewById(R.id.textViewNombreUsuario)

        val sessionController = SessionController(this)
        val usuario = sessionController.getSession()

        if (usuario != null) {
            // Mostrar el nombre del usuario y otros detalles
            textViewBienvenido.text = "CLIENTES"
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
        // Crear un Intent para ir a la pantalla del menu productos
        val intent = Intent(this, MenuPrincipalActivity::class.java)

        // Iniciar la actividad NuevoProductoActivity
        startActivity(intent)

        // Finalizar la actividad de login para que el usuario no pueda regresar
        finish()
    }


    // Método para el botón Nuevo Cliente
    fun nuevoClienteonClick(view: View) {
        val intent = Intent(this, NuevoClienteActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Método para el botón Eliminar Cliente
    fun eliminarClienteonClick(view: View) {
        val intent = Intent(this, EliminarClienteActivity::class.java)
        // Iniciar la actividad NuevoProductoActivity
        startActivity(intent)
        // Finalizar la actividad de login para que el usuario no pueda regresar
        finish()
    }

    // Método para el botón Eliminar Cliente
    fun editarProductoonClick(view: View) {
        val intent = Intent(this, EditarClienteActivity::class.java)
        startActivity(intent)
        finish()
    }

}
