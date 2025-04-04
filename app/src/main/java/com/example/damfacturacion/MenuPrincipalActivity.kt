package com.example.damfacturacion

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.controller.SessionController
import com.example.damfacturacion.views.HelpDeskActivity
import com.example.damfacturacion.views.clientes.MenuClientesActivity
import com.example.damfacturacion.views.facturacion.NewFactClientActivity
import com.example.damfacturacion.views.productos.MenuProductosActivity

class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var textViewBienvenido: TextView
    private lateinit var textViewNombreUsuario: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        // Inicializar las vistas
        textViewBienvenido = findViewById(R.id.textViewBienvenido)
        textViewNombreUsuario = findViewById(R.id.textViewNombreUsuario)

        val sessionController = SessionController(this)
        val usuario = sessionController.getSession()

        if (usuario != null) {
            // Mostrar el nombre del usuario y otros detalles
            textViewBienvenido.text = "BIENVENIDO"
            textViewNombreUsuario.text = "${usuario.nombre}"
        } else {
            // Manejar el caso cuando no hay sesión
        }
    }

    // Método para el botón Salir
    fun onLogoutClick(view: View) {
        val sessionController = SessionController(this)
        sessionController.clearSession()  // Cerrar sesión

        // Redirigir al Login
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // Finalizar la actividad actual
    }


    // Método para el botón ir a PRODUCTOS
    fun menuProductosonClick(view: View) {
        // Crear un Intent para ir al menu productos
        val intent = Intent(this, MenuProductosActivity::class.java)

        // Iniciar la actividad MenuPrincipalActivity
        startActivity(intent)

        // Finalizar la actividad de login para que el usuario no pueda regresar
        finish()
    }

    fun goOrdenaPlusonClick(view: View) {
        val url = "https://www.ordenaplus.com"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    // Método para el botón ir a CLIENTES
    fun menuClientesonClick(view: View) {
        // Crear un Intent para ir al menu clientes
        val intent = Intent(this, MenuClientesActivity::class.java)
        // Iniciar la actividad MenuClientesActivity
        startActivity(intent)
        // Finalizar la actividad de login para que el usuario no pueda regresar
        finish()
    }

    // Método para el botón ir a CLIENTES
    fun helpdeskonClick(view: View) {
        val intent = Intent(this, HelpDeskActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Método para el botón ir a VENTAS
    fun menuVentasonClick(view: View) {
        val intent = Intent(this, NewFactClientActivity::class.java)
        startActivity(intent)
        finish()
    }

}
