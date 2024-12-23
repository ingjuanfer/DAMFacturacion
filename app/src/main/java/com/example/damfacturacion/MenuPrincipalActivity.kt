package com.example.damfacturacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.controller.SessionController
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


    // Método para el botón Salir
    fun menuProductosonClick(view: View) {
        // Crear un Intent para ir a la pantalla principal
        val intent = Intent(this, MenuProductosActivity::class.java)

        // Iniciar la actividad MenuPrincipalActivity
        startActivity(intent)

        // Finalizar la actividad de login para que el usuario no pueda regresar
        finish()
    }

}
