package com.application.ravenrankboost.View.ListOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.application.ravenrankboost.R
import kotlinx.android.synthetic.main.item_list_order.view.*

class ListOrderAdapter(val context: Context, var list: List<ListOrderModel>) : BaseAdapter() {
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
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_order, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        when {
            list[position].id < 10 -> {
                holder.tvId.text = "#000${list[position].id}"
            }
            list[position].id in 11..99 -> {
                holder.tvId.text = "#00${list[position].id}"
            }
            else -> {
                holder.tvId.text = "#0${list[position].id}"
            }
        }
        holder.tvUsername.text = list[position].username
        holder.ivCurrentRank.setImageResource(list[position].orderedRank)
        holder.ivWantedRank.setImageResource(list[position].wantedRank)
        holder.tvStatus.text = list[position].status

        return view
    }

    inner class ViewHolder(view: View) {
        val tvUsername = view.tvUsername!!
        val tvId = view.tvId!!
        val ivCurrentRank = view.ivCurrentRank!!
        val ivWantedRank = view.ivWantedRank!!
        val tvStatus = view.tvStatus!!
    }

}