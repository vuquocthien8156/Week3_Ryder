package com.example.qthien.week3_ryder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_recycler_cmt.view.*

class AdapterRecylerCmt(var con : Context , var array: ArrayList<String>) : RecyclerView.Adapter<AdapterRecylerCmt.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(con).inflate(R.layout.item_recycler_cmt , p0 , false))
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtCmt.text = array[p1]
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var txtCmt : TextView = itemView.txtCmt
    }
}