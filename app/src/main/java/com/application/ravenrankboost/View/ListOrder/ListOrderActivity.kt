package com.application.ravenrankboost.View.ListOrder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.application.ravenrankboost.R
import com.application.ravenrankboost.Data.RankDatas
import com.application.ravenrankboost.Databases.DatabaseOrderHelper
import com.application.ravenrankboost.Helper.SessionManager
import com.application.ravenrankboost.View.AdminOrder.AdminOrderActivity
import com.application.ravenrankboost.View.OrderDetails.OrderDetailActivity
import kotlinx.android.synthetic.main.activity_list_order.*

class ListOrderActivity : AppCompatActivity() {
    lateinit var list: ArrayList<ListOrderModel>
    lateinit var db: DatabaseOrderHelper
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_order)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        list = ArrayList()
        db = DatabaseOrderHelper(this)
        sessionManager = SessionManager(this)

        if (sessionManager.isAdmin()){
            btBack.isVisible = false
            btSignOut.isVisible = true
            btSignOut.setOnClickListener { view ->
                sessionManager.endSessionLogout()
            }
        }

        val cursor = db.allData
        while (cursor.moveToNext()) {
            val model = ListOrderModel()
            if (sessionManager.isAdmin()) {
                model.id = cursor.getInt(0)
                model.username = cursor.getString(2)
                for (i in RankDatas.rankstext.indices) {
                    if (RankDatas.rankstext[i] == cursor.getString(4)) {
                        model.orderedRank = RankDatas.ranks[i]
                    }
                    if (RankDatas.rankstext[i] == cursor.getString(6)) {
                        model.wantedRank = RankDatas.ranks[i]
                    }
                    if (RankDatas.rankstext[i] == cursor.getString(5)) {
                        model.currentRank = RankDatas.ranks[i]
                    }
                }
                model.status = cursor.getString(8)
                model.statusGame = cursor.getString(8)
                list.add(model)
            } else {
                if (cursor.getString(1) == sessionManager.getStringFromSP("EMAIL")) {
                    model.id = cursor.getInt(0)
                    model.username = cursor.getString(2)
                    for (i in RankDatas.rankstext.indices) {
                        if (RankDatas.rankstext[i] == cursor.getString(4)) {
                            model.orderedRank = RankDatas.ranks[i]
                        }
                        if (RankDatas.rankstext[i] == cursor.getString(6)) {
                            model.wantedRank = RankDatas.ranks[i]
                        }
                        if (RankDatas.rankstext[i] == cursor.getString(5)) {
                            model.currentRank = RankDatas.ranks[i]
                        }
                    }
                    model.status = cursor.getString(8)
                    model.statusGame = cursor.getString(9)
                    list.add(model)
                }
            }
        }

        list.reverse()
        lvListOrder.adapter = ListOrderAdapter(this, list)
        lvListOrder.onItemClickListener = AdapterView.OnItemClickListener { p0, p1, position, p3 ->
            if (sessionManager.isAdmin()) {
                startActivity(Intent(this@ListOrderActivity, AdminOrderActivity::class.java).apply {
                    putExtra("orderId", list[position].id)
                    putExtra("username", list[position].username)
                    putExtra("orderedRank", list[position].orderedRank)
                    putExtra("currentRank", list[position].currentRank)
                    putExtra("wantedRank", list[position].wantedRank)
                    putExtra("status", list[position].statusGame)
                })
            } else {
                startActivity(
                    Intent(
                        this@ListOrderActivity,
                        OrderDetailActivity::class.java
                    ).apply {
                        putExtra("orderId", list[position].id)
                        putExtra("username", list[position].username)
                        putExtra("orderedRank", list[position].orderedRank)
                        putExtra("currentRank", list[position].currentRank)
                        putExtra("wantedRank", list[position].wantedRank)
                        putExtra("status", list[position].statusGame)
                    })
            }
        }

        btBack.setOnClickListener { view ->
            onBackPressed()
        }
    }

}