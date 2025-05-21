package com.example.awita

import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import firebase.com.protolitewrapper.BuildConfig


class AcercaDeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_acerca_de, container, false)

        val tvVersion = vista.findViewById<TextView>(R.id.tv_version)
        val tvContacto = vista.findViewById<TextView>(R.id.tv_contacto)

        val versionName = BuildConfig.VERSION_NAME
        tvVersion.text = "Versión de la app: $versionName"

        Linkify.addLinks(tvContacto, Linkify.EMAIL_ADDRESSES or Linkify.WEB_URLS)

        return vista
    }
}
