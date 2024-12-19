package com.example.damfacturacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.damfacturacion.ui.theme.DAMFacturacionTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DAMFacturacionTheme {
                // Variable de estado para controlar la pantalla actual
                var showLoginScreen by remember { mutableStateOf(true) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showLoginScreen) {
                        LoginScreen {
                            // Acción después del inicio de sesión exitoso
                            showLoginScreen = false // Muestra la siguiente pantalla
                        }
                    } else {
                        // Pantalla que se muestra después del inicio de sesión
                        //Greeting("Bienvenidos") // Reemplaza esto con tu pantalla principal
                        MainScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DAMFacturacionTheme {
        Greeting("Android")
    }
}

@Composable
fun MainScreen() {
    Text("Esta es la pantalla principal")
}