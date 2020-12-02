package com.application.ravenrankboost.View.AdminOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.application.ravenrankboost.R
import kotlinx.android.synthetic.main.item_spinner_rank_new_order.view.*

class RankAdapter(val context: Context,var list : List<RankModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val holder : ViewHolder

        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_spinner_rank_new_order,parent,false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        holder.tvRank.text = list[position].rankText
        holder.ivRank.setImageResource(list[position].rankImage)

        return view
    }

    inner class ViewHolder(view: View){
        val tvRank = view.tvRank!!
        val ivRank = view.ivRank!!
    }

}