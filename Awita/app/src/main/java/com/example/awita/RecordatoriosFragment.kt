package com.example.awita

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.*

class RecordatoriosFragment : Fragment() {

    private lateinit var switch: Switch
    private lateinit var spinner: Spinner
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_recordatorios, container, false)

        switch = view.findViewById(R.id.switch_recordatorios)
        spinner = view.findViewById(R.id.spinner_intervalo)

        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.intervalos_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            val interval = spinner.selectedItem.toString().toIntOrNull() ?: 1
            if (isChecked) {
                programarAlarma(interval)
            } else {
                cancelarAlarma()
            }
        }

        return view
    }

    private fun programarAlarma(intervaloHoras: Int) {
        val intent = Intent(requireContext(), RecordatorioReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val intervaloMillis = intervaloHoras * 60 * 60 * 1000L
        val triggerAtMillis = System.currentTimeMillis() + intervaloMillis

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            intervaloMillis,
            pendingIntent
        )
        Toast.makeText(requireContext(), "Recordatorios activados", Toast.LENGTH_SHORT).show()
    }

    private fun cancelarAlarma() {
        val intent = Intent(requireContext(), RecordatorioReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(requireContext(), "Recordatorios desactivados", Toast.LENGTH_SHORT).show()
    }
}
