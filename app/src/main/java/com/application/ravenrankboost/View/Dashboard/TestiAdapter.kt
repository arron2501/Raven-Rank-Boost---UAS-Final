package com.application.ravenrankboost.View.Dashboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.ravenrankboost.R
import kotlinx.android.synthetic.main.item_dashboard.view.*

class TestiAdapter(val context: Context, var list: List<Int>) :
    RecyclerView.Adapter<TestiAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivItem.setImageResource(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val ivItem = item.ivItem
    }

}