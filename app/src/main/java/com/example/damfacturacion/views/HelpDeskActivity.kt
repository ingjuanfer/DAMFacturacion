package com.example.damfacturacion.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.MenuPrincipalActivity
import com.example.damfacturacion.R
import com.example.damfacturacion.views.productos.MenuProductosActivity

class HelpDeskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helpdesk)
    }


    // Método para el botón que es el logo de la app para regresar al menu Productos
    fun menuPrincipalonClick(view: View) {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        startActivity(intent)
        finish()
    }

}
