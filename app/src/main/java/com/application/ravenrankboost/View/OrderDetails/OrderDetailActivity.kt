package com.application.ravenrankboost.View.OrderDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.application.ravenrankboost.Data.RankDatas
import com.application.ravenrankboost.Data.RankDatas.ranks
import com.application.ravenrankboost.Data.RankDatas.rankstext
import com.application.ravenrankboost.Databases.DatabaseMatchHelper
import com.application.ravenrankboost.R
import kotlinx.android.synthetic.main.activity_order_detail.*

class OrderDetailActivity : AppCompatActivity() {
    lateinit var list: ArrayList<OrderDetailModel>
    lateinit var db: DatabaseMatchHelper
    var orderId = 0
    var username = ""
    var orderedRank = 0
    var currentRank = 0
    var wantedRank = 0
    var status = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        btBack.setOnClickListener { view ->
            onBackPressed()
        }
        list = ArrayList()
        db = DatabaseMatchHelper(this)
        orderId = intent.getIntExtra("orderId", 0)
        username = intent.getStringExtra("username")!!
        orderedRank = intent.getIntExtra("orderedRank", 0)
        currentRank = intent.getIntExtra("currentRank", 0)
        wantedRank = intent.getIntExtra("wantedRank", 0)
        status = intent.getStringExtra("status")!!

        var orderedRankText = ""
        var wantedRankText = ""

         val orderIdConverted = when {
            orderId < 10 -> {
                "#000${orderId}"
            }
            orderId in 11..99 -> {
                "#00${orderId}"
            }
            else -> {
                "#0${orderId}"
            }
        }

        tv_Id_Username.text = "ORDER NO $orderIdConverted - $username"
        ivCurrentRank.setImageResource(orderedRank)
        ivWantedRank.setImageResource(wantedRank)
        var o = 0
        var w = 0
        var c = 0

        for (i in ranks.indices) {
            if (ranks[i] == orderedRank) {
                orderedRankText = rankstext[i]
                o = i
            }
            if (ranks[i] == currentRank) {
                c = i
            }
            if (ranks[i] == wantedRank) {
                wantedRankText = rankstext[i]
                w = i
            }
        }
        tvCurrentRank.text = orderedRankText
        tvWantedRank.text = wantedRankText
        progressOrder.progress = ((c - o) * 100) / (w - o)

        tvStatus.text = "STATUS : $status"
        if (status == "OFF-GAME") {
            gameStatus.text = "YOU CAN NOW SIGN IN INTO THE GAME!"
        }
        else {
            gameStatus.text = "PLEASE WAIT, YOU CAN'T SIGN IN INTO THE GAME YET!"
        }

        val cursor = db.allData
        while (cursor.moveToNext()) {
            if (cursor.getInt(1) == orderId) {
                val model = OrderDetailModel()
                model.result = cursor.getInt(2) == 1
                model.score = cursor.getString(3)
                model.kda = cursor.getString(4)
                list.add(model)
            }
        }
        list.reverse()
        lvMatchList.adapter = OrderDetailAdapter(this, list)
    }
}