package com.example.awita

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment

class MetasFragment : Fragment() {

    private lateinit var inputMeta: EditText
    private lateinit var switchRecordatorios: Switch
    private lateinit var btnGuardar: Button

    private val PREFS_NAME = "datos_agua"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_metas, container, false)

        inputMeta = vista.findViewById(R.id.input_meta)
        switchRecordatorios = vista.findViewById(R.id.switch_recordatorios)
        btnGuardar = vista.findViewById(R.id.btn_guardar_metas)

        cargarPreferencias()

        btnGuardar.setOnClickListener {
            guardarPreferencias()
        }

        return vista
    }

    private fun cargarPreferencias() {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val meta = prefs.getInt("meta_diaria", 2000)
        val recordatorios = prefs.getBoolean("recordatorios_activados", false)

        inputMeta.setText(meta.toString())
        switchRecordatorios.isChecked = recordatorios
    }

    private fun guardarPreferencias() {
        val metaTexto = inputMeta.text.toString()
        val meta = if (metaTexto.isNotEmpty()) metaTexto.toInt() else 2000
        val recordatorios = switchRecordatorios.isChecked

        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt("meta_diaria", meta)
            .putBoolean("recordatorios_activados", recordatorios)
            .apply()

        Toast.makeText(requireContext(), "Metas guardadas correctamente", Toast.LENGTH_SHORT).show()
    }
}
