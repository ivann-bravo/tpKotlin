package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        sharedPreferences = getSharedPreferences("Preferencia", MODE_PRIVATE)

        val mainFragment = MainFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, mainFragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val mainActivityItem = menu.findItem(R.id.action_main_activity)
        mainActivityItem.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_mostrar_hits -> {
                val intent = Intent(this, MostrarHits::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_mostrar_albumes -> {
                val intent = Intent(this, AlbumListActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_cerrar_sesion -> {
                // Borra los datos de sesión y redirige a la pantalla de inicio de sesión
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
