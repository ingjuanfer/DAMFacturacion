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
import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient

class MenuClientesActivity  : AppCompatActivity() {

    private lateinit var textViewBienvenido: TextView
    private lateinit var textViewNombreUsuario: TextView
    private lateinit var webViewVerifactuInfo: WebView // Añadir referencia al WebView

    @SuppressLint("SetJavaScriptEnabled") // Necesario para habilitar JavaScript
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_cliente)

        // Inicializar las vistas
        textViewBienvenido = findViewById(R.id.textViewBienvenido)
        textViewNombreUsuario = findViewById(R.id.textViewNombreUsuario)
        webViewVerifactuInfo = findViewById(R.id.webViewVerifactuInfo) // Inicializar WebView

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

        // --- Configurar el WebView ---
        // URL obtenida de la búsqueda (Página oficial de la Agencia Tributaria sobre VeriFactu)
        val urlVerifactu = "https://x.com/haciendagob?lang=es"
            //"https://sede.agenciatributaria.gob.es/Sede/iva/sistemas-informaticos-facturacion-verifactu.html"

        // Habilitar JavaScript (muchas páginas lo necesitan)
        webViewVerifactuInfo.settings.javaScriptEnabled = true

        // Para que los enlaces se abran dentro del WebView y no en el navegador externo
        webViewVerifactuInfo.webViewClient = WebViewClient()

        // Cargar la URL
        webViewVerifactuInfo.loadUrl(urlVerifactu)
        // --- Fin de configuración del WebView ---
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
