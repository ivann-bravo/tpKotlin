package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.bumptech.glide.Glide
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar

class MostrarHits : AppCompatActivity() {

    private lateinit var editTextArtistName: EditText
    private lateinit var buttonSearch: Button
    private lateinit var textViewArtistInfo: TextView
    private lateinit var imageViewSongArt: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_hits)

        editTextArtistName = findViewById(R.id.editTextArtistName)
        buttonSearch = findViewById(R.id.buttonSearch)
        textViewArtistInfo = findViewById(R.id.textViewArtistInfo)
        imageViewSongArt = findViewById(R.id.imageViewSongArt)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        buttonSearch.setOnClickListener {
            val artistName = editTextArtistName.text.toString()
            Log.d("Artist Name", artistName)

            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val accessToken = "0yW9JyawFKHpO5Z-yzRMk4ICl8dxnfKHwvNiQY0EHQwYkYjwPEPzFxJ9fvdM6JF_"
                    val response = ApiService.geniusApiService.searchArtistInfo("Bearer $accessToken", artistName)
                    Log.d("API Response", response.toString())
                    if (response.isSuccessful) {
                        val searchResponse = response.body()
                        runOnUiThread {
                            if (searchResponse != null && searchResponse.response.hits.isNotEmpty()) {
                                val artist = searchResponse.response.hits[0].result
                                textViewArtistInfo.text = "Título de la canción: ${artist.titleWithFeatured}\nFecha de lanzamiento: ${artist.releaseDateForDisplay}"
                                Glide.with(this@MostrarHits)
                                    .load(artist.songArtImageUrl)
                                    .into(imageViewSongArt)
                            } else {
                                textViewArtistInfo.text = "Artista no encontrado."
                            }
                        }
                    } else {
                        runOnUiThread {
                            textViewArtistInfo.text = "Error en la solicitud."
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    textViewArtistInfo.text = "Error: ${e.message}"
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val mostrarHitsItem = menu.findItem(R.id.action_mostrar_hits)
        mostrarHitsItem.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_main_activity -> {
                val intent = Intent(this, MainActivity::class.java)
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