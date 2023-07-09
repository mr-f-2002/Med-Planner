@file:Suppress("DEPRECATION")

package com.example.navbar

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.car.ui.toolbar.Tab
import com.example.navbar.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout


class HomeFragment(mode: String = "MORNING") : Fragment() {
    private lateinit var comunitacor: Comunitacor
    private lateinit var inputDialog: Dialog
    private lateinit var myUri: Uri
    private var Mode = mode

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
   //     MedicineData.writeData(requireContext())
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val addMedicineBtn: FloatingActionButton = view.findViewById(R.id.addMedicineBtn)
        val tab =  view.findViewById<TabLayout>(R.id.timeTab)
        val recyclerView = view.findViewById<RecyclerView>(R.id.medListView)
        comunitacor = activity as Comunitacor
        addMedicineBtn.setOnClickListener {
            inputDialog = Dialog(requireContext())
            inputDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            inputDialog.setContentView(R.layout.fragment_input)
            inputDialog.setCancelable(false)
            inputDialog.findViewById<Button>(R.id.cnclBtn).setOnClickListener{
                inputDialog.dismiss()
            }
            inputDialog.findViewById<FloatingActionButton>(R.id.pickImage).setOnClickListener {
                ImagePicker.with(this)
                    .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            }
            inputDialog.findViewById<Button>(R.id.submitBtn).setOnClickListener{
                // grabbing data
                val image = inputDialog.findViewById<ImageView>(R.id.imgBtn)
                val name = inputDialog.findViewById<EditText>(R.id.editTextText)?.text.toString()
                val stock = inputDialog.findViewById<EditText>(R.id.editTextNumberDecimal)?.text.toString()
                val morningSwitch = inputDialog.findViewById<Switch>(R.id.morningSwtich)
                val noonSwitch = inputDialog.findViewById<Switch>(R.id.noonSwitch)
                val nightSwitch = inputDialog.findViewById<Switch>(R.id.nightSwitch)

                // helping variables
                var morning = R.drawable.checked
                var noon = R.drawable.checked
                var night = R.drawable.checked


                // setting data to create parameter
                if (!morningSwitch.isChecked) morning = R.drawable.unchecked
                if (!noonSwitch.isChecked) noon = R.drawable.unchecked
                if (!nightSwitch.isChecked) night = R.drawable.unchecked

                if(name!="" && stock!="") {
                    MedicineData.addData(myUri.toString(), name, stock.toInt(), morning, noon, night)
                    val recyclerView = view.findViewById<RecyclerView>(R.id.medListView)
                    recyclerView.adapter?.notifyItemInserted(MedicineData.getData().size - 1)
                }
                inputDialog.dismiss()
                fragmentManager?.beginTransaction()?.replace(R.id.framelayout, HomeFragment(MedicineData.getTimeStatus()))?.commit()
            }
            inputDialog.show()

        }


        // recycler view
        var adapter:MyAdapter
        if(Mode == "MORNING") {
            tab.getTabAt(0)?.select()
            adapter = MyAdapter(MedicineData.getMorningData(), requireContext())
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
            adapter.setOnClickListerer(object : MyAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    val data = MedicineData.getMorningData()
                    comunitacor.passData(
                        data[position].medImg,
                        data[position].medName,
                        data[position].medStock,
                        data[position].morningImg,
                        data[position].noonImg,
                        data[position].nightImg
                    )
                }
            })
        } else if(Mode == "NOON"){
            tab.getTabAt(1)?.select()
            adapter = MyAdapter(MedicineData.getNoonData(), requireContext())
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
            adapter.setOnClickListerer(object : MyAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    val data = MedicineData.getNoonData()
                    comunitacor.passData(
                        data[position].getUri().toString(),
                        data[position].medName,
                        data[position].medStock,
                        data[position].morningImg,
                        data[position].noonImg,
                        data[position].nightImg
                    )
                }
            })
        } else if(Mode == "NIGHT"){
            tab.getTabAt(2)?.select()
            adapter = MyAdapter(MedicineData.getNightData(), requireContext())
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
            adapter.setOnClickListerer(object : MyAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    val data = MedicineData.getNightData()
                    comunitacor.passData(
                        data[position].medImg,
                        data[position].medName,
                        data[position].medStock,
                        data[position].morningImg,
                        data[position].noonImg,
                        data[position].nightImg
                    )
                }
            })
        }

        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                when (position) {
                    0 -> fragmentManager?.beginTransaction()?.replace(R.id.framelayout, HomeFragment("MORNING"))?.commit()
                    1 -> fragmentManager?.beginTransaction()?.replace(R.id.framelayout, HomeFragment("NOON"))?.commit()
                    2 -> fragmentManager?.beginTransaction()?.replace(R.id.framelayout, HomeFragment("NIGHT"))?.commit()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val position = tab?.position
                when (position) {
                    0 -> fragmentManager?.beginTransaction()?.replace(R.id.framelayout, HomeFragment("MORNING"))?.commit()
                    1 -> fragmentManager?.beginTransaction()?.replace(R.id.framelayout, HomeFragment("NOON"))?.commit()
                    2 -> fragmentManager?.beginTransaction()?.replace(R.id.framelayout, HomeFragment("NIGHT"))?.commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inputDialog.findViewById<ImageView>(R.id.imgBtn).setImageURI(data?.data)
        myUri = data?.data!!
    }

}