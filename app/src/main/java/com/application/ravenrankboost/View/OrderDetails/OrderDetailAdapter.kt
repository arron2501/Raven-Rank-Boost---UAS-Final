package com.application.ravenrankboost.View.OrderDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.application.ravenrankboost.R
import kotlinx.android.synthetic.main.item_match_list.view.*

class OrderDetailAdapter(val context: Context, var list: List<OrderDetailModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return if (list.size > 3) 3
        else list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_match_list, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        if (list[position].result) {
            holder.layoutMatchItem.background = context.getDrawable(R.drawable.victory_box)
            holder.tvMatchStatus.text = "VICTORY!"
        } else {
            holder.layoutMatchItem.background = context.getDrawable(R.drawable.defeat_box)
            holder.tvMatchStatus.text = "DEFEAT!"
        }

        holder.tvMatchScore.text = list[position].score
        holder.tvMatchKda.text = list[position].kda

        return view
    }

    inner class ViewHolder(view: View) {
        val layoutMatchItem = view.layout_match_item!!
        val tvMatchStatus = view.tvMatchStatus!!
        val tvMatchScore = view.tvMatchScore!!
        val tvMatchKda = view.tvMatchKda!!
    }
}