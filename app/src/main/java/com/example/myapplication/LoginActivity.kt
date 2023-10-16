package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var checkBoxRemember: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userDao = AppDatabase.getInstance(application).userDao()
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        checkBoxRemember = findViewById(R.id.checkBoxRemember)
        val buttonLogin: Button = findViewById(R.id.buttonLogin)

        // Verifica si el usuario está marcado como recordado y carga los datos si es necesario
        val sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", "")
        val savedPassword = sharedPreferences.getString("password", "")
        val isChecked = sharedPreferences.getBoolean("isChecked", false)
        val buttonRegister: Button = findViewById(R.id.buttonRegister)

        if (isChecked) {

            editTextUsername.setText(savedUsername)
            editTextPassword.setText(savedPassword)
            checkBoxRemember.isChecked = true

            val name = "Recordar Usuario"
            val descriptionText = "Canal para notificaciones de recordar usuario"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("recordarUsuarioChannel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        buttonRegister.setOnClickListener {
            // Abre la actividad de registro cuando se hace clic en el botón "Registrarme"
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            val rememberUser = checkBoxRemember.isChecked

            GlobalScope.launch(Dispatchers.IO) {
                val user = userDao.getUserByUsername(username)

                // Verificar si el usuario existe y la contraseña es correcta
                if (user != null && user.password == password) {
                    runOnUiThread {
                        // Login exitoso
                        Toast.makeText(this@LoginActivity, "Login exitoso, bienvenido " + user.username, Toast.LENGTH_SHORT)
                            .show()

                        // Guardar credenciales si el usuario marcó la casilla de recordar usuario
                        if (rememberUser) {
                            val editor = getSharedPreferences("loginPrefs", MODE_PRIVATE).edit()
                            editor.putString("username", username)
                            editor.putString("password", password)
                            editor.putBoolean("isChecked", true)
                            editor.apply()

                            // Crear y mostrar la notificación de usuario recordado
                            userRememberedNotification()
                        }

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    runOnUiThread {
                        // Credenciales incorrectas, muestra un mensaje de error.
                        Toast.makeText(
                            this@LoginActivity,
                            "Credenciales incorrectas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

    private fun userRememberedNotification() {
        val builder = NotificationCompat.Builder(this, "recordarUsuarioChannel")
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("Usuario recordado")
            .setContentText("Tus credenciales han sido recordadas.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }
}
