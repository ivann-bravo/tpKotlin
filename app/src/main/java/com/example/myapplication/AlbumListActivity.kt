package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlbumListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        recyclerView = findViewById(R.id.albumRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val albums = listOf(
            Album("The Bends", "Radiohead", R.drawable.bends),
            Album("Pablo Honey", "Radiohead", R.drawable.pablohoney1),
            Album("Kid A", "Radiohead", R.drawable.kid),
            Album("Amnesiac", "Radiohead", R.drawable.amne),
            Album("Hail to the Thief", "Radiohead", R.drawable.hail),
        )

        val adapter = AlbumAdapter(albums) { album ->

        }

        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val mostrarAlbumesItem = menu.findItem(R.id.action_mostrar_albumes)
        mostrarAlbumesItem.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_main_activity -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_mostrar_hits -> {
                val intent = Intent(this, MostrarHits::class.java)
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
