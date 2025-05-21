package com.example.awita

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

class ConfiguracionFragment : Fragment() {

    private lateinit var radioMl: RadioButton
    private lateinit var radioOz: RadioButton
    private lateinit var switchSonido: Switch
    private lateinit var switchVibracion: Switch
    private lateinit var switchTema: Switch

    private val prefs by lazy {
        requireContext().getSharedPreferences("config", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val vista = inflater.inflate(R.layout.fragment_configuracion, container, false)

        radioMl = vista.findViewById(R.id.rb_ml)
        radioOz = vista.findViewById(R.id.rb_oz)
        switchSonido = vista.findViewById(R.id.switch_sonido)
        switchVibracion = vista.findViewById(R.id.switch_vibracion)
        switchTema = vista.findViewById(R.id.switch_tema)

        cargarPreferencias()
        configurarEventos()

        return vista
    }

    private fun cargarPreferencias() {
        val unidad = prefs.getString("unidad", "ml")
        radioMl.isChecked = unidad == "ml"
        radioOz.isChecked = unidad == "oz"

        switchSonido.isChecked = prefs.getBoolean("sonido", true)
        switchVibracion.isChecked = prefs.getBoolean("vibracion", true)
        switchTema.isChecked = prefs.getBoolean("modo_oscuro", false)
    }

    private fun configurarEventos() {
        radioMl.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) prefs.edit().putString("unidad", "ml").apply()
        }
        radioOz.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) prefs.edit().putString("unidad", "oz").apply()
        }

        switchSonido.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("sonido", isChecked).apply()
        }

        switchVibracion.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("vibracion", isChecked).apply()
        }

        switchTema.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("modo_oscuro", isChecked).apply()
            val modo = if (isChecked)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO

            AppCompatDelegate.setDefaultNightMode(modo)
        }
    }
}
