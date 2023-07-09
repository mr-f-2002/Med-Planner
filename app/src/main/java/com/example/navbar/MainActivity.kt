package com.example.navbar

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.navbar.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), Comunitacor {
    lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        MedicineData.loadMyDataListFromFile(this, "Medicines.json")
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Loading medicine list according to time
        replaceFragmentById(HomeFragment(MedicineData.getTimeStatus()))

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home ->replaceFragmentById(HomeFragment(MedicineData.getTimeStatus()))
                R.id.chat -> replaceFragmentById(ChatFragment())
                R.id.setting -> replaceFragmentById(MapsFragment())
                R.id.alarm -> replaceFragmentById(AlarmFragment())
                else -> true
            }

        }

        if(MedicineData.getShortageData().size > 0) {
            val badge: BadgeDrawable = binding.bottomNavigationView.getOrCreateBadge(R.id.chat)
            badge.number = MedicineData.getShortageData().size
            badge.isVisible = true
        }
    }

    override fun onPause() {
        super.onPause()
        MedicineData.storeMyDataListToFile(this, MedicineData.getData(), "Medicines.json")
    }

    override fun onDestroy() {
        MedicineData.storeMyDataListToFile(this, MedicineData.getData(), "Medicines.json")
        super.onDestroy()
    }

    fun replaceFragmentById(fragment: Fragment): Boolean {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.framelayout, fragment)
            .commit()
        return true
    }

    override fun passData(medImg: String?, medName: String?, medStock: Int?, morningImg: Int?, noonImg: Int?, nightImg: Int?) {
        val bundle = Bundle()
        bundle.putString("medImg", medImg)
        bundle.putString("medName", medName)
        bundle.putInt("medStock", medStock!!)
        bundle.putInt("morningImg", morningImg!!)
        bundle.putInt("noonImg", noonImg!!)
        bundle.putInt("nightImg", nightImg!!)

        var detailsFragment = DetailsFragment()
        detailsFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.framelayout, detailsFragment).addToBackStack("homeFragment").commit()
    }

}