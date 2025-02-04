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
import java.security.MessageDigest
import android.util.Base64
import androidx.activity.viewModels
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var empresaEditText: EditText
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
        empresaEditText = findViewById(R.id.editTextEmpresa)
        loginButton = findViewById(R.id.buttonLogin)
        progressBar = findViewById(R.id.progressBar)

        // Configurar el botón de login
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val empresa = empresaEditText.text.toString().trim().lowercase()

            // Mostrar el ProgressBar (indicador de carga)
            progressBar.visibility = ProgressBar.VISIBLE

            // Validación de campos vacíos
            if (username.isEmpty() || password.isEmpty() || empresa.isEmpty()) {
                Toast.makeText(this, "Usuario o clave o Empresa no pueden estar vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Encriptar empresa usando AES
            val encryptedEmpresa = encryptAES(empresa, "EQSISSAS")

            // Validación de Token
            if (empresa != "ordenaplus") {
                Toast.makeText(this, "Empresa no valida!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Mostrar el ProgressBar (indicador de carga)
            progressBar.visibility = View.VISIBLE

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

    private fun encryptAES(plainText: String, key: String): String {
        return try {
            // Generar clave AES desde SHA-512 y truncarla a 256 bits (32 bytes)
            val passwordBytes = key.toByteArray(StandardCharsets.UTF_8)
            val sha512 = MessageDigest.getInstance("SHA-512").digest(passwordBytes)
            val aesKey = sha512.copyOf(32) // Solo los primeros 32 bytes

            // Configurar AES en modo ECB (sin IV) con padding PKCS5
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val secretKeySpec = SecretKeySpec(aesKey, "AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)

            // Encriptar los datos
            val encryptedBytes = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))

            // Convertir a Base64 (sin saltos de línea)
            Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            "Error en la encriptación"
        }
    }

}
