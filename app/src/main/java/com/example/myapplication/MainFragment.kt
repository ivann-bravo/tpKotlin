package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val showHitsButton = view.findViewById<Button>(R.id.showHitsButton)
        val showAlbumsButton = view.findViewById<Button>(R.id.showAlbumsButton)
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)

        val sharedPreferences = requireContext().getSharedPreferences("Preferencia", Context.MODE_PRIVATE)

        showHitsButton.setOnClickListener {
            // Iniciar la actividad para mostrar hits
            val intent = Intent(requireContext(), MostrarHits::class.java)
            startActivity(intent)
        }

        showAlbumsButton.setOnClickListener {
            // Iniciar la actividad para mostrar álbumes
            val intent = Intent(requireContext(), AlbumListActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            // Borra los datos de sesión y redirige a la pantalla de inicio de sesión
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }
}
