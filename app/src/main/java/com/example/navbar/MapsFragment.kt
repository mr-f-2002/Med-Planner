package com.example.navbar

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    lateinit var gMap:GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        gMap = googleMap
        val my_home = LatLng(24.433385535984115, 90.77113524999999)
        googleMap.addMarker(MarkerOptions().position(my_home).title("Marker in my_home"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my_home, 15f))
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        val popupMenu = PopupMenu(context, view.findViewById(R.id.MapTypeSelectBtn))
        popupMenu.menuInflater.inflate(R.menu.map_manu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem->
            when(menuItem.itemId){
                R.id.normal-> {
                    gMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                    true
                }
                R.id.hybrid-> {
                    gMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
                    true
                }
                R.id.satellite-> {
                    gMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                    true
                }
                R.id.terrain-> {
                    gMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                    true
                }
                else->{true}
            }
        }

        view.findViewById<ImageButton>(R.id.MapTypeSelectBtn).setOnClickListener{
            popupMenu.show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}