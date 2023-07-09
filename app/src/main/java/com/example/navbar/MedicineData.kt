package com.example.navbar

import android.content.Context
import android.net.Uri
import android.os.Build
import android.text.BoringLayout
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object MedicineData {
    private val list: ArrayList<MyModel> = ArrayList()

    fun getData():ArrayList<MyModel>{
        return list
    }

    fun getMorningData():ArrayList<MyModel>{
        var temp = ArrayList<MyModel>()
        for (i in list)
            if (i.morningImg == R.drawable.checked)
                temp.add(i)
        return temp
    }

    fun getNoonData():ArrayList<MyModel>{
        var temp = ArrayList<MyModel>()
        for (i in list)
            if (i.noonImg == R.drawable.checked)
                temp.add(i)
        return temp
    }

    fun getNightData():ArrayList<MyModel>{
        var temp = ArrayList<MyModel>()
        for (i in list)
            if (i.nightImg == R.drawable.checked)
                temp.add(i)
        return temp
    }

    fun getShortageData():ArrayList<MyModel>{
        var temp = ArrayList<MyModel>()
        for (i in list)
            if(i.medStock!! <= 3)
                temp.add(i)
        return temp
    }

    fun addData(medImg: String?, medName: String, medStock: Int, morning: Int, noon:Int, night:Int){
        list.add(MyModel(medImg, medName, medStock, morning, noon, night))
    }

    fun updateMedStock(name: String, amount: Int):Boolean{
        for (i in list){
            if (i.medName == name){
                if(i.medStock!! >= 1) {
                    i.medStock = i.medStock?.plus(amount)
                    return true
                }
            }
        }
        return false
    }

    fun updateMed(image:String?, oldName:String, newName:String, stock:Int, morning:Int, noon:Int, night:Int){
        for (i in list){
            if (i.medName == oldName){
                i.medImg = image
                i.medName = newName
                i.medStock = stock
                i.morningImg = morning
                i.noonImg = noon
                i.nightImg = night
            }
        }
    }

    fun deleteMed(name: String){
        var index: Int
        var temp = ArrayList<MyModel>()
        for (i in list)
            temp.add(i)
        list.clear()
        for(i in temp) {
            if (i.medName == name)
                continue
            else
                list.add(i)
        }

    }

    fun writeData(context: Context): Boolean{
        val fileName = "MedData.txt"
        val fileContents = list.toString()
        Log.d("xxxx",fileContents)
        val file = File(context.filesDir, fileName)
        file.writeText(fileContents)
        Toast.makeText(context, "changes saved", Toast.LENGTH_SHORT).show()
        return true
    }

    fun loadData(context: Context) {
        val fileName = "/data/data/com.example.navbar/files/MedData.txt"
        val file = File(fileName)
        if (file.exists()) {
            val content = file.readText()
            val pattern = Regex("medImg=(\\d+), medName=(\\w+), medStock=(\\d+), morningImg=(\\d+), noonImg=(\\d+), nightImg=(\\d+)")
            val matches = pattern.findAll(content)
            for (match in matches) {
                val a = match.groupValues[1].toInt()
                val b = match.groupValues[2]
                val c = match.groupValues[3].toInt()
                val d = match.groupValues[4].toInt()
                val e = match.groupValues[5].toInt()
                val f = match.groupValues[6].toInt()

                Log.d("xxxx","medImg=$a, medName=$b, medStock=$c, morningImg=$d, noonImg=$e, nightImg=$f")
                list.add(MyModel(a.toString(),b,c,d,e,f))
            }
            Toast.makeText(context, "data loaded", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("xxxx","File not found.")
        }

//        list.add(MyModel(R.drawable.m1, "Cyprocin", 15, R.drawable.checked, R.drawable.unchecked, R.drawable.checked))
//        list.add(MyModel(R.drawable.m2, "Paracetamol", 15, R.drawable.checked, R.drawable.unchecked, R.drawable.checked))
//        list.add(MyModel(R.drawable.m3, "Ibuprofen", 10, R.drawable.unchecked, R.drawable.checked, R.drawable.checked))
//        list.add(MyModel(R.drawable.m4, "Aspirin", 5, R.drawable.unchecked, R.drawable.checked, R.drawable.unchecked))
//        list.add(MyModel(R.drawable.m5, "Omeprazole", 2, R.drawable.checked, R.drawable.unchecked, R.drawable.checked))
//        list.add(MyModel(R.drawable.m6, "Simvastatin", 8, R.drawable.checked, R.drawable.checked, R.drawable.unchecked))
//        list.add(MyModel(R.drawable.m7, "Metformin", 12, R.drawable.checked, R.drawable.checked, R.drawable.unchecked))
//        list.add(MyModel(R.drawable.m8, "Sertraline", 7, R.drawable.checked, R.drawable.unchecked, R.drawable.checked));
//        list.add(MyModel(R.drawable.m9, "Losartan", 25, R.drawable.checked, R.drawable.unchecked, R.drawable.unchecked))
//        list.add(MyModel(R.drawable.m1, "Albuterol", 11, R.drawable.unchecked, R.drawable.checked, R.drawable.checked))
//        list.add(MyModel(R.drawable.m2, "Diphenhydramine", 13, R.drawable.unchecked, R.drawable.unchecked, R.drawable.checked))
//        list.add(MyModel(R.drawable.m3, "Metoprolol", 2, R.drawable.unchecked, R.drawable.unchecked, R.drawable.unchecked))
//        list.add(MyModel(R.drawable.m4, "Levothyroxine", 9, R.drawable.checked, R.drawable.unchecked, R.drawable.unchecked))
//        list.add(MyModel(R.drawable.m5, "Warfarin", 1, R.drawable.checked, R.drawable.checked, R.drawable.unchecked))
//        list.add(MyModel(R.drawable.m6, "Pantoprazole", 1, R.drawable.unchecked, R.drawable.checked, R.drawable.unchecked))
//        list.add(MyModel(R.drawable.m7, "Max Pro 20", 30, R.drawable.unchecked, R.drawable.unchecked, R.drawable.checked))

    }

    fun storeMyDataListToFile(context: Context, myDataList: ArrayList<MyModel>, filename: String) {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val json: String = gson.toJson(myDataList)
        val file = File(context.filesDir, filename)
        val outputStream = FileOutputStream(file)
        val writer = OutputStreamWriter(outputStream)
        writer.write(json)
        writer.close()
        outputStream.close()
        Toast.makeText(context, "data stored", Toast.LENGTH_SHORT).show()
    }


    fun loadMyDataListFromFile(context: Context, filename: String) {
        val file = File(context.filesDir, filename)
        if (file.exists()) {
            val inputStream = FileInputStream(file)
            val reader = InputStreamReader(inputStream)
            val gson: Gson = GsonBuilder().registerTypeAdapter(Uri::class.java, UriDeserializer()).create()
            val type: Type = object : TypeToken<ArrayList<MyModel>>() {}.type
            try {
            val myDataList: ArrayList<MyModel> = gson.fromJson(reader, type)
            reader.close()
            inputStream.close()
            for (item in myDataList) list.add(item)
            Toast.makeText(context, "data loaded", Toast.LENGTH_SHORT).show()
            }catch (e:Exception){ }
        }
    }



    class UriDeserializer : JsonDeserializer<Uri> {
        override fun deserialize(json: com.google.gson.JsonElement, typeOfT: Type?, context: com.google.gson.JsonDeserializationContext?): Uri {
            return Uri.parse(json.asString)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeStatus(): String{
        val currentHour = LocalTime.now().format(DateTimeFormatter.ofPattern("HH")).toInt()
        if(currentHour in 4..12)
            return "MORNING"
        else if(currentHour in 13..16)
            return "NOON"
        else
            return "NIGHT"
    }




    // for my image
    lateinit var uri: Uri
    fun setImageUri(uuri:Uri){
        uri = uuri
    }

    fun getUriForImage(medName: String): Uri? {
        for (item in list){
            if(item.medName == medName)
                return item.getUri()
        }
        return null
    }
}