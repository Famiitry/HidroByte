package com.example.awita

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class RecordatorioReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "¡Hora de tomar agua! 💧", Toast.LENGTH_LONG).show()
    }
}
