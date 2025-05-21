package com.example.awita

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.Date
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Locale

class InicioFragment : Fragment() {

    private lateinit var tvMeta: TextView
    private lateinit var tvConsumo: TextView
    private lateinit var progreso: ProgressBar
    private lateinit var inputCantidad: EditText
    private lateinit var btnRegistrar: Button

    private var metaDiaria = 0 // ✅ Se cargará dinámicamente
    private var consumoActual = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_inicio, container, false)

        tvMeta = vista.findViewById(R.id.tv_meta_diaria)
        tvConsumo = vista.findViewById(R.id.tv_consumo_actual)
        progreso = vista.findViewById(R.id.progreso_agua)
        inputCantidad = vista.findViewById(R.id.input_cantidad)
        btnRegistrar = vista.findViewById(R.id.btn_registrar)

        progreso.max = metaDiaria
        actualizarUI()

        btnRegistrar.setOnClickListener {
            val cantidadTexto = inputCantidad.text.toString()
            if (cantidadTexto.isNotEmpty()) {
                val cantidad = cantidadTexto.toInt()
                consumoActual += cantidad
                guardarConsumo(consumoActual)
                guardarEnHistorial(cantidad) // 👉 Guardamos también en historial
                actualizarUI()
                inputCantidad.text.clear()
            } else {
                Toast.makeText(requireContext(), "Ingresa una cantidad válida", Toast.LENGTH_SHORT).show()
            }
        }

        return vista
    }

    private fun actualizarUI() {
        tvMeta.text = "Meta diaria: ${metaDiaria} ml"
        tvConsumo.text = "Hoy has tomado: ${consumoActual} ml"
        progreso.progress = consumoActual
    }

    override fun onResume() {
        super.onResume()
        metaDiaria = obtenerMetaDiaria()         // 👈 NUEVO
        consumoActual = obtenerConsumo()
        actualizarUI()
    }

    private fun obtenerMetaDiaria(): Int {
        val prefs = requireContext().getSharedPreferences("datos_agua", Context.MODE_PRIVATE)
        return prefs.getInt("meta_diaria", 2000) // 2000 como valor por defecto
    }


    private fun guardarConsumo(valor: Int) {
        val prefs = requireContext().getSharedPreferences("datos_agua", Context.MODE_PRIVATE)
        prefs.edit().putInt("consumo_hoy", valor).apply()
    }

    private fun obtenerConsumo(): Int {
        val prefs = requireContext().getSharedPreferences("datos_agua", Context.MODE_PRIVATE)
        return prefs.getInt("consumo_hoy", 0)
    }
    private fun guardarEnHistorial(cantidad: Int) {
        val sharedHistorial =
            requireContext().getSharedPreferences("Historial", Context.MODE_PRIVATE)
        val currentDate = SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date())
        val previous = sharedHistorial.getInt(currentDate, 0)
        sharedHistorial.edit().putInt(currentDate, previous + cantidad).apply()
    }
}
