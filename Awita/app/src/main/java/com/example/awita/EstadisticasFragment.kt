package com.example.awita

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class EstadisticasFragment : Fragment() {

    private lateinit var tvResumen: TextView
    private lateinit var barChart: BarChart
    private val metaDiaria = 2000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val vista = inflater.inflate(R.layout.fragment_estadisticas, container, false)
        tvResumen = vista.findViewById(R.id.tv_resumen_estadisticas)
        barChart = vista.findViewById(R.id.bar_chart)

        mostrarEstadisticas()
        return vista
    }

    private fun mostrarEstadisticas() {
        val sharedHistorial = requireContext().getSharedPreferences("Historial", Context.MODE_PRIVATE)
        val calendario = Calendar.getInstance()
        val formato = SimpleDateFormat("dd/MM", Locale.getDefault())

        val entradas = ArrayList<BarEntry>()
        val etiquetas = ArrayList<String>()

        var totalDias = 0
        var diasCumplidos = 0
        var totalConsumo = 0

        for (i in 6 downTo 0) { // últimos 7 días
            calendario.time = Date()
            calendario.add(Calendar.DAY_OF_YEAR, -i)
            val fecha = formato.format(calendario.time)
            val consumo = sharedHistorial.getInt(fecha, 0)

            etiquetas.add(fecha)
            entradas.add(BarEntry((6 - i).toFloat(), consumo.toFloat()))

            if (consumo > 0) {
                totalDias++
                totalConsumo += consumo
                if (consumo >= metaDiaria) diasCumplidos++
            }
        }

        val promedio = if (totalDias > 0) totalConsumo / totalDias else 0
        val porcentaje = if (totalDias > 0) (diasCumplidos * 100) / totalDias else 0

        val resumen = """
            📅 Últimos $totalDias días registrados
            ✅ $diasCumplidos días cumpliste la meta
            💧 Promedio diario: $promedio ml
            📈 Porcentaje de cumplimiento: $porcentaje%
        """.trimIndent()
        tvResumen.text = resumen

        val dataSet = BarDataSet(entradas, "Consumo diario (ml)")
        dataSet.color = Color.parseColor("#2196F3")

        val data = BarData(dataSet)
        data.barWidth = 0.9f
        barChart.data = data

        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(etiquetas)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
        barChart.invalidate()
    }
}
