package com.example.awita

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class HistorialFragment : Fragment() {

    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)
        barChart = view.findViewById(R.id.barChart)
        mostrarGrafico()
        return view
    }

    private fun mostrarGrafico() {
        val sharedPreferences = requireContext().getSharedPreferences("Historial", Context.MODE_PRIVATE)
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        val calendar = Calendar.getInstance()

        for (i in 6 downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val fecha = dateFormat.format(calendar.time)
            val consumo = sharedPreferences.getInt(fecha, 0)

            entries.add(BarEntry((6 - i).toFloat(), consumo.toFloat()))
            labels.add(fecha)
        }

        val dataSet = BarDataSet(entries, "Agua (ml)")
        dataSet.color = resources.getColor(R.color.teal_700, null)
        val data = BarData(dataSet)
        data.barWidth = 0.9f

        barChart.data = data
        barChart.setFitBars(true)
        barChart.description.isEnabled = false

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return labels.getOrElse(value.toInt()) { "" }
            }
        }

        barChart.invalidate() // refrescar
    }
}
