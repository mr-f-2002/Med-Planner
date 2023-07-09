package com.example.navbar

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout


class SettingFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        val btn = view.findViewById<Button>(R.id.fuck)
        val tab =  view.findViewById<TabLayout>(R.id.tabLayout)
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                when (position) {
                    0 -> Toast.makeText(context, "Tab 1 selected", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(context, "Tab 2 selected", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(context, "Tab 3 selected", Toast.LENGTH_SHORT).show()
                    // Add more cases for additional tabs if needed
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
        return view
    }


}