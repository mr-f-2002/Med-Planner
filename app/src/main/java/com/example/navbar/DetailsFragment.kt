package com.example.navbar

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton


@Suppress("DEPRECATION")
class DetailsFragment : Fragment() {
    lateinit var editDialogBox: Dialog
    private lateinit var myUri: Uri
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val imgStr = arguments?.getString("medImg")
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        view.findViewById<TextView>(R.id.dMedName).text = arguments?.getString("medName")
        view.findViewById<TextView>(R.id.dMedStock).text = "Available : "+arguments?.getInt("medStock").toString()
        view.findViewById<ImageView>(R.id.dMedImg).setImageResource(requireArguments().getInt("medImg"))
        view.findViewById<ImageView>(R.id.morningImg).setImageResource(requireArguments().getInt("morningImg"))
        view.findViewById<ImageView>(R.id.dNoonImg).setImageResource(requireArguments().getInt("noonImg"))
        view.findViewById<ImageView>(R.id.dNightImg).setImageResource(requireArguments().getInt("nightImg"))
        var med_name = view.findViewById<TextView>(R.id.dMedName).text.toString()
        var med_stock = view.findViewById<TextView>(R.id.dMedStock).text.toString()
        val numberPattern = "\\d+".toRegex()
        med_stock = numberPattern.find(med_stock)!!.value
        var med_morning = false
        var med_noon = false
        var med_night = false
        if(requireArguments().getInt("morningImg") == R.drawable.checked) med_morning = true
        if(requireArguments().getInt("noonImg") == R.drawable.checked) med_noon = true
        if(requireArguments().getInt("nightImg") == R.drawable.checked) med_night = true

        view.findViewById<ImageView>(R.id.dMedImg).setImageURI(MedicineData.getUriForImage(med_name))



        val backBtn: ImageButton = view.findViewById(R.id.backBtn)
        backBtn.setOnClickListener{
            fragmentManager?.beginTransaction()?.replace(R.id.framelayout, HomeFragment(MedicineData.getTimeStatus()))?.commit()
        }

        var editBtn = view.findViewById<FloatingActionButton>(R.id.editBtn)
        editBtn.setOnClickListener {
            editDialogBox = Dialog(requireContext())
            editDialogBox.requestWindowFeature(Window.FEATURE_NO_TITLE)
            editDialogBox.setContentView(R.layout.fragment_input)
            editDialogBox.setCancelable(false)

            editDialogBox.findViewById<EditText>(R.id.editTextText).setText(med_name)
            editDialogBox.findViewById<EditText>(R.id.editTextNumberDecimal).setText(med_stock)
            editDialogBox.findViewById<Switch>(R.id.morningSwtich).isChecked = med_morning
            editDialogBox.findViewById<Switch>(R.id.noonSwitch).isChecked = med_noon
            editDialogBox.findViewById<Switch>(R.id.nightSwitch).isChecked = med_night

            Toast.makeText(requireContext(), "edit data", Toast.LENGTH_SHORT).show()
            editDialogBox.findViewById<Button>(R.id.cnclBtn).setOnClickListener{
                editDialogBox.dismiss()
            }
            editDialogBox.findViewById<FloatingActionButton>(R.id.pickImage).setOnClickListener{
                ImagePicker.with(this)
                    .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            }
            editDialogBox.findViewById<Button>(R.id.submitBtn).setOnClickListener{
                // grabbing data
                val name = editDialogBox.findViewById<EditText>(R.id.editTextText)?.text.toString()
                val stock = editDialogBox.findViewById<EditText>(R.id.editTextNumberDecimal)?.text.toString()
                val morningSwitch = editDialogBox.findViewById<Switch>(R.id.morningSwtich)
                val noonSwitch = editDialogBox.findViewById<Switch>(R.id.noonSwitch)
                val nightSwitch = editDialogBox.findViewById<Switch>(R.id.nightSwitch)

                // helping variables
                var morning = R.drawable.checked
                var noon = R.drawable.checked
                var night = R.drawable.checked

                // setting data to create parameter
                if (!morningSwitch.isChecked) morning = R.drawable.unchecked
                if (!noonSwitch.isChecked) noon = R.drawable.unchecked
                if (!nightSwitch.isChecked) night = R.drawable.unchecked

                if(name!="" && stock!="") {
                    myUri = MedicineData.getUriForImage(med_name)!!
                    MedicineData.updateMed(myUri.toString(), med_name, name, stock.toInt(), morning, noon, night)
                }
                editDialogBox.dismiss()
                fragmentManager?.beginTransaction()?.replace(R.id.framelayout, HomeFragment(MedicineData.getTimeStatus()))?.commit()
            }
            editDialogBox.show()

        }

        var deleteBtn = view.findViewById<FloatingActionButton>(R.id.deleteBtn)
        deleteBtn.setOnClickListener{
            MedicineData.deleteMed(med_name)
            Toast.makeText(context, "$med_name deleted", Toast.LENGTH_SHORT).show()
            fragmentManager?.beginTransaction()?.replace(R.id.framelayout, HomeFragment(MedicineData.getTimeStatus()))?.commit()
        }

        var takenBtn = view.findViewById<Button>(R.id.takenBtn)
        takenBtn.setOnClickListener {
            val result = MedicineData.updateMedStock(med_name, -1)
            if (result) {
                Toast.makeText(context, "$med_name Taken", Toast.LENGTH_SHORT).show()
                fragmentManager?.beginTransaction()?.replace(R.id.framelayout, HomeFragment(MedicineData.getTimeStatus()))
                    ?.commit()
            }else{
                Toast.makeText(context, "$med_name is out of stock!!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        editDialogBox.findViewById<ImageView>(R.id.imgBtn).setImageURI(data?.data)
        myUri = data?.data!!
    }

}