package com.application.ravenrankboost.View.AdminOrder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import com.application.ravenrankboost.Data.RankDatas.ranks
import com.application.ravenrankboost.Data.RankDatas.rankstext
import com.application.ravenrankboost.Databases.DatabaseMatchHelper
import com.application.ravenrankboost.Databases.DatabaseNotifHelper
import com.application.ravenrankboost.Databases.DatabaseOrderHelper
import com.application.ravenrankboost.Helper.SessionManager
import com.application.ravenrankboost.R
import com.application.ravenrankboost.View.ListOrder.ListOrderActivity
import kotlinx.android.synthetic.main.activity_admin_order.*
import kotlinx.android.synthetic.main.activity_admin_order.btBack
import kotlinx.android.synthetic.main.activity_admin_order.ivCurrentRank
import kotlinx.android.synthetic.main.activity_admin_order.ivWantedRank
import kotlinx.android.synthetic.main.activity_admin_order.tv_Id_Username
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdminOrderActivity : AppCompatActivity() {
    lateinit var listCurrent: ArrayList<RankModel>
    lateinit var listStatus: ArrayList<RankModel>
    lateinit var listMatch: ArrayList<RankModel>
    lateinit var dbOrder: DatabaseOrderHelper
    lateinit var dbMatch: DatabaseMatchHelper
    lateinit var dbNotif: DatabaseNotifHelper
    lateinit var sessionManager: SessionManager
    var orderId = 0
    var username = ""
    var orderedRank = 0
    var currentRank = 0
    var wantedRank = 0
    var status = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_order)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        sessionManager = SessionManager(this)
        listCurrent = ArrayList()
        dbOrder = DatabaseOrderHelper(this)
        dbMatch = DatabaseMatchHelper(this)
        dbNotif = DatabaseNotifHelper(this)
        orderId = intent.getIntExtra("orderId", 0)
        username = intent.getStringExtra("username")!!
        orderedRank = intent.getIntExtra("orderedRank", 0)
        currentRank = intent.getIntExtra("currentRank", 0)
        wantedRank = intent.getIntExtra("wantedRank", 0)
        status = intent.getStringExtra("status")!!
        var orderIdConverted = ""
        var orderedRankText = ""
        var wantedRankText = ""

        orderIdConverted = when {
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
        var c = 0
        var w = 0

        for (i in ranks.indices) {
            if (ranks[i] == orderedRank) {
                orderedRankText = rankstext[i]
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

        btBack.setOnClickListener { view ->
            onBackPressed()
        }

        for (i in c until w+1) {
            if (i < ranks.size) {
                val model = RankModel()
                model.rankText = rankstext[i]
                model.rankImage = ranks[i]
                listCurrent.add(model)
            }
        }

        spnCurrentRank.adapter = RankAdapter(this, listCurrent)

        listStatus = ArrayList()
        var statusModel = RankModel()
        statusModel.rankText = "STATUS"
        statusModel.rankImage = R.drawable.logomark_navy
        listStatus.add(statusModel)
        statusModel = RankModel()
        statusModel.rankText = "IN-GAME"
        listStatus.add(statusModel)
        statusModel = RankModel()
        statusModel.rankText = "OFF-GAME"
        listStatus.add(statusModel)
        spnStatus.adapter = RankAdapter(this, listStatus)

        if (status == "ON PROGRESS") {
            spnStatus.setSelection(1, true)
        } else {
            spnStatus.setSelection(2, true)
        }


        listMatch = ArrayList()
        var matchModel = RankModel()
        matchModel.rankText = "MATCH HISTORY"
        matchModel.rankImage = R.drawable.logomark_navy
        listMatch.add(matchModel)
        matchModel = RankModel()
        matchModel.rankText = "VICTORY!"
        listMatch.add(matchModel)
        matchModel = RankModel()
        matchModel.rankText = "DEFEAT!"
        listMatch.add(matchModel)
        spnMatchResult.adapter = RankAdapter(this, listMatch)

        tvReset.setOnClickListener { view ->
            spnCurrentRank.setSelection(0, true)
            spnStatus.setSelection(0, true)
            spnMatchResult.setSelection(0, true)
            etVictory.setText("")
            etDefeat.setText("")
            etKill.setText("")
            etDeath.setText("")
            etAssist.setText("")
        }

        btAdd.setOnClickListener { view ->
            if (validate()) {
                val result = spnMatchResult.selectedItemPosition == 1
                val score =
                    "${etVictory.text.toString().trim()} - ${etDefeat.text.toString().trim()}"
                val kda = "${etKill.text.toString().trim()}/${
                    etDeath.text.toString().trim()
                }/${etAssist.text.toString().trim()}"

                val currentRank = listCurrent[spnCurrentRank.selectedItemPosition].rankText
                val statusGame = listStatus[spnStatus.selectedItemPosition].rankText
                val cursor = dbOrder.allData
                var status = ""
                var addNotif = false
                var addNotifCompleted = false
                var email = ""
                while (cursor.moveToNext()) {
                    status =
                        if (orderId == cursor.getInt(0) && currentRank == cursor.getString(6)) {
                            "COMPLETED"
                        } else {
                            "ON PROGRESS"
                        }
                    email =
                        if (orderId == cursor.getInt(0)) cursor.getString(1)
                        else ""

                    addNotif = orderId == cursor.getInt(0) && currentRank != cursor.getString(5)
                    addNotifCompleted =
                        orderId == cursor.getInt(0) && currentRank == cursor.getString(6)
                }


                if (dbMatch.insertdata(
                        orderId,
                        result,
                        score,
                        kda
                    ) && dbOrder.updateStatus(
                        orderId.toString(),
                        status
                    ) && dbOrder.updateStatusGame(
                        orderId.toString(),
                        statusGame
                    ) && dbOrder.updateCurrentRank(orderId.toString(), currentRank)
                ) {
                    toast("Success")
                    val monthNames = arrayOf(
                        "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                        "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
                    )
                    val date = Date()
                    val year = SimpleDateFormat("yyyy", Locale.US).format(Date())
                    val getDate = "${date.date} ${monthNames[date.month]} $year"

                    if (addNotif) {
                        dbNotif.insertdata(
                            email,
                            getDate,
                            "$orderIdConverted($username) telah mencapai rank $currentRank"
                        )
                    }
                    if (addNotifCompleted) {
                        dbNotif.insertdata(
                            email,
                            getDate,
                            "Order $orderIdConverted($username) telah selesai"
                        )
                    }
                    Handler().postDelayed(
                        Runnable {
                            startActivity(
                                Intent(
                                    this@AdminOrderActivity,
                                    ListOrderActivity::class.java
                                ).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                })
                            finish()
                        }, 1000
                    )
                }
            }

        }
    }

    fun validate(): Boolean {
        var valid = true
        if (spnStatus.selectedItemPosition == 0 || spnMatchResult.selectedItemPosition == 0 || etVictory.text.toString()
                .trim().isBlank() || etDefeat.text.toString().trim()
                .isBlank() || etKill.text.toString().trim().isBlank() || etDeath.text.toString()
                .trim().isBlank() || etAssist.text.toString().trim().isBlank()
        ) {
            valid = false
        }
        return valid
    }

}