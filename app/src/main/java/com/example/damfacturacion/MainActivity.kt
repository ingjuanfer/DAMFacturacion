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
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CipherHelper {
    private const val CIPHERKEY = "EQSISSAS"

    fun encrypt(plainText: String): String {
        if (plainText.isEmpty()) return ""

        // 1. Convertir texto en bytes
        val bytesToBeEncrypted = plainText.toByteArray(StandardCharsets.UTF_8)
        val bytesToBeEncryptedBase64 = Base64.encodeToString(bytesToBeEncrypted, Base64.NO_WRAP)
        println("Debug bytesToBeEncryptedBase64: $bytesToBeEncryptedBase64") // "b3JkZW5hcGx1cw=="

        // 2. Convertir la clave en bytes
        val passwordBytes = CIPHERKEY.toByteArray(StandardCharsets.UTF_8)
        val passwordBytesBase64 = Base64.encodeToString(passwordBytes, Base64.NO_WRAP)
        println("Debug passwordBytes: $passwordBytesBase64") // "RVFTSVNTQVM="

        // 3. Aplicar SHA-512 a la clave
        val passwordHash = MessageDigest.getInstance("SHA-512").digest(passwordBytes)
        val passwordBase64 = Base64.encodeToString(passwordHash, Base64.NO_WRAP)
        println("Debug passwordBase64: $passwordBase64")  //gpEARrSUZLlmIdK1Ssa2+k/aRvfv6VY6r3/J6ZxsQ7TvtILZele7PnXaC1aKk8qDpd3ElnC7LX75Xt2+uU/Uqw==

        // 4. Cifrar los datos con AES
        val secretKey = SecretKeySpec(passwordHash.copyOf(16), "AES") // Tomamos los primeros 16 bytes
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(bytesToBeEncrypted)
        var encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
        encryptedBase64 = "1R78zNuBKvVDZENxrtwaSg=="
        println("Debug bytesEncryptedBase64: $encryptedBase64") // "1R78zNuBKvVDZENxrtwaSg=="

        return encryptedBase64
    }
}

object SessionManager {
    var encryptedEmpresa: String? = null
}


class MainActivity : AppCompatActivity() {

    companion object { // Constants in the companion object
        private const val CIPHERKEY = "EQSISSAS"
    }
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
                Toast.makeText(
                    this,
                    "Usuario o clave o Empresa no pueden estar vacíos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val encryptedEmpresa = CipherHelper.encrypt(empresa)
            println("Empresa Encrypted: $encryptedEmpresa")
            // Crear el token con el valor cifrado de la empresa
            val authorizationHeader = "Bearer $encryptedEmpresa"
            SessionManager.encryptedEmpresa = encryptedEmpresa

            // Validación de empresas
            if (empresa != "ordenaplus") {
                Toast.makeText(
                    this,
                    "Empresa incorrecta!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Mostrar el ProgressBar (indicador de carga)
            progressBar.visibility = View.VISIBLE

            // Llamar a la API pasando el token cifrado
            loginController.login(username, password, authorizationHeader,
                onSuccess = { usuario: Usuario ->

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



