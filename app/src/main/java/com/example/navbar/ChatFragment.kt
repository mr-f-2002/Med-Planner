package com.example.navbar

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class ChatFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_chat, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.medShortageListView)
        rv.layoutManager = GridLayoutManager(requireContext(), 3)
        rv.adapter = RvAdapter(requireContext(), MedicineData.getShortageData())

        view.findViewById<ImageView>(R.id.findMedicineShop).setOnClickListener{
            val searchKeyWord = Uri.encode("pharmacy around me")
            val gmmIntentUri = Uri.parse("geo:0,0?q=$searchKeyWord")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                val mapsUrl = "https://www.google.com/maps/search/?api=1&query=$searchKeyWord"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl))
                startActivity(browserIntent)
            }
        }

        return view
    }

}