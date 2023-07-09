package com.example.navbar

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RvAdapter(val requiredContext: Context, val medData: ArrayList<MyModel>): RecyclerView.Adapter<RvAdapter.viewHolder>(){
    class viewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val medImg = itemView.findViewById<ImageView>(R.id.gMedImg)
        val medName = itemView.findViewById<TextView>(R.id.gMedName)
        val medStock = itemView.findViewById<TextView>(R.id.gMedStock)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid_layout, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
       return medData.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.medImg.setImageURI(medData[position].getUri())
        holder.medName.text = medData[position].medName
        holder.medStock.text = medData[position].medStock.toString()+" left"
    }

}