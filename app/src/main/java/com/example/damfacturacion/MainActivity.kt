package com.example.damfacturacion

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.damfacturacion.controller.LoginController
import com.example.damfacturacion.controller.SessionController
import com.example.damfacturacion.model.Usuario

class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar  // Declarar ProgressBar



    // Usamos 'viewModels()' para obtener la instancia de LoginController
    private val loginController: LoginController by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar vistas
        usernameEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        progressBar = findViewById(R.id.progressBar)

        // Configurar el botón de login
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Mostrar el ProgressBar (indicador de carga)
            progressBar.visibility = ProgressBar.VISIBLE

            // Validación de campos vacíos
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Usuario o clave no pueden estar vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginController.login(username, password,
                onSuccess = { usuario: Usuario  ->

                    // Guardar la información del usuario en la sesión
                    val sessionController = SessionController(this)
                    sessionController.saveSession(usuario)

                    // Ocultar el ProgressBar una vez que termine el proceso
                    progressBar.visibility = ProgressBar.GONE

                    // Mostrar mensaje de éxito con el nombre del usuario
                    Toast.makeText(this, "Bienvenido ${usuario.nombre}", Toast.LENGTH_SHORT).show()

                    // Crear un Intent para ir a la pantalla principal
                    val intent = Intent(this, MenuPrincipalActivity::class.java)

                    // Pasar el nombre del usuario al Intent
                    intent.putExtra("NOMBRE_USUARIO", usuario.nombre)

                    // Iniciar la actividad MenuPrincipalActivity
                    startActivity(intent)

                    // Finalizar la actividad de login para que el usuario no pueda regresar
                    finish()
                },
                onFailure = { errorMessage ->

                    // Ocultar el ProgressBar una vez que termine el proceso
                    progressBar.visibility = ProgressBar.GONE

                    // Mostrar el mensaje de error
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )


        }
    }
}
