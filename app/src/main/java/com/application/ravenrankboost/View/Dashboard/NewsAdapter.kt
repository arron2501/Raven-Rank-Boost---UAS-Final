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


class NewsAdapter(val context: Context, var list: List<Int>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    var holder : ViewHolder? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivItem.setImageResource(list[position])
        this.holder = holder
        holder!!.ivItem.setOnClickListener { view ->
            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                if (position == 0) {
                    val url = "https://www.youtube.com/watch?v=xOFv16NbvCU"
                    data = Uri.parse(url)
                }
                if (position == 1) {
                    val url = "https://playvalorant.com/en-us/news/game-updates/valorant-patch-notes-1-11/"
                    data = Uri.parse(url)
                }
                if (position == 2) {
                    val url = "https://playvalorant.com/en-us/news/game-updates/valorant-patch-notes-1-10/"
                    data = Uri.parse(url)
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val ivItem = item.ivItem
    }
}