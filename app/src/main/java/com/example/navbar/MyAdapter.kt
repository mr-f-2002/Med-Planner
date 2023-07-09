package com.example.navbar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager.AccessibilityServicesStateChangeListener
import androidx.recyclerview.widget.RecyclerView
import com.example.navbar.databinding.ItemLayoutBinding

class MyAdapter(var medList: ArrayList<MyModel>, var context: Context):RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
    lateinit var clickListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnClickListerer(listener: onItemClickListener){
        clickListener = listener
    }
    inner class MyViewHolder(var viewBinging: ItemLayoutBinding, listener: onItemClickListener):RecyclerView.ViewHolder(viewBinging.root){
        init{
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var viewBinging = ItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(viewBinging, clickListener)
    }

    override fun getItemCount(): Int {return medList.size}

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.viewBinging.medImg.setImageURI(medList[position].getUri())
        holder.viewBinging.medName.text = medList[position].medName
        holder.viewBinging.medStock.text = medList[position].medStock.toString()
        holder.viewBinging.morningImg.setImageResource(medList[position].morningImg!!)
        holder.viewBinging.noonImg.setImageResource(medList[position].noonImg!!)
        holder.viewBinging.nightImg.setImageResource(medList[position].nightImg!!)
    }
}