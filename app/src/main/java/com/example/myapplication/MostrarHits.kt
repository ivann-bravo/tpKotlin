package com.example.myapplication

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

class MostrarHits : AppCompatActivity() {

    private lateinit var editTextArtistName: EditText
    private lateinit var buttonSearch: Button
    private lateinit var textViewArtistInfo: TextView
    private lateinit var imageViewSongArt: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_hits)
        editTextArtistName = findViewById(R.id.editTextArtistName)
        buttonSearch = findViewById(R.id.buttonSearch)
        textViewArtistInfo = findViewById(R.id.textViewArtistInfo)
        imageViewSongArt = findViewById(R.id.imageViewSongArt)

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
}