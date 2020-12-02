package com.application.ravenrankboost.View.Notifications

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.application.ravenrankboost.R
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter(val context: Context, var list: List<NotificationModel>) : BaseAdapter() {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        if (list[position].type == "notif") {
            holder.layoutDate.setBackgroundColor(Color.WHITE)
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            params.setMargins(30, 20, 30, 20);
            val typeface = ResourcesCompat.getFont(context, R.font.dinnextltpro_regular)
            holder.tvDate.layoutParams = params
        } else if (list[position].type == "date") {
            holder.layoutDate.setBackgroundResource(R.color.notifitemcolordate)
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            params.setMargins(30, 0, 30, 0);
            holder.tvDate.layoutParams = params
        }

        holder.tvDate.text = list[position].text
        return view
    }

    inner class ViewHolder(view: View) {
        val layoutDate = view.layoutDate!!
        val tvDate = view.tvDate!!
    }
}