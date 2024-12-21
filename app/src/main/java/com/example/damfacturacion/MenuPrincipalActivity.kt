package com.example.damfacturacion

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var textViewBienvenido: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        // Obtener el nombre del usuario desde el Intent
        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO")

        // Inicializar las vistas
        textViewBienvenido = findViewById(R.id.textViewBienvenido)

        // Mostrar el mensaje de bienvenida con el nombre del usuario
        textViewBienvenido.text = "Bienvenido, $nombreUsuario"
    }
}
