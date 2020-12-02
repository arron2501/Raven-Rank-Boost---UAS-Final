package com.application.ravenrankboost.View.NewOrder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.application.ravenrankboost.Data.RankDatas.ranks
import com.application.ravenrankboost.Data.RankDatas.rankstext
import com.application.ravenrankboost.Databases.DatabaseOrderHelper
import com.application.ravenrankboost.Helper.SessionManager
import com.application.ravenrankboost.R
import com.application.ravenrankboost.View.Dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_new_order.*
import kotlinx.android.synthetic.main.activity_terms_of_service.*
import org.jetbrains.anko.toast

class NewOrderActivity : AppCompatActivity() {
    lateinit var listCurrent: ArrayList<RankModel>
    lateinit var listWanted: ArrayList<RankModel>
    lateinit var sessionManager: SessionManager
    lateinit var db: DatabaseOrderHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        sessionManager = SessionManager(this)
        db = DatabaseOrderHelper(this)

        setupSpinner()
        tvTerms.setOnClickListener { view ->
            cbTerm.isChecked = !cbTerm.isChecked
            val builder = AlertDialog.Builder(this)
            val view = LayoutInflater.from(this).inflate(R.layout.activity_terms_of_service,null,false)
            builder.setView(view)
            val alertDialog = builder.create()
            alertDialog.show()
        }
        ivBack.setOnClickListener { view ->
            onBackPressed()
        }
        btCheckout.setOnClickListener { view ->
            if (validate()) {
                val email = sessionManager.getStringFromSP("EMAIL")
                val username = etUsername.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val orderedRank = listCurrent[spnCurrentRank.selectedItemPosition].rankText
                val currentRank = listCurrent[spnCurrentRank.selectedItemPosition].rankText
                val wantedRank = listWanted[spnWantedRank.selectedItemPosition].rankText
                var c = 0
                var w = 0

                for (i in rankstext.indices) {
                    if (currentRank == rankstext[i]) {
                        c = i
                    }
                    if (wantedRank == rankstext[i]) {
                        w = i
                    }
                }
                val subTotal = "IDR ${((w - c) * 20)}.000,-"
                val status = "ON PROGRESS"
                val statusGame = "IN-GAME"
                if (db.insertdata(
                        email,
                        username,
                        password,
                        orderedRank,
                        currentRank,
                        wantedRank,
                        subTotal,
                        status,
                        statusGame
                    )
                ) {
                    toast("Success")
                    Handler().postDelayed(
                        Runnable {
                            startActivity(
                                Intent(
                                    this@NewOrderActivity,
                                    DashboardActivity::class.java
                                ).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                })
                        }, 1000
                    )
                } else {
                    toast("Failed")
                }
            } else {
                toast("Semua field harus diisi")
            }
        }

    }

    fun validate(): Boolean {
        var valid = true
        if (etUsername.text.toString().trim().isBlank()) {
            valid = false
        }
        if (etPassword.text.toString().trim().isBlank()) {
            valid = false
        }
        if (spnCurrentRank.selectedItemPosition == 0) {
            valid = false
        }
        if (!cbTerm.isChecked) {
            valid = false
            toast("CEKLIS WOI!!!!!!!!")
        }
        return valid
    }

    fun setupSpinner() {
        listCurrent = ArrayList()
        listWanted = ArrayList()

        val currentModel = RankModel()
        currentModel.rankText = "CURRENT RANK"
        currentModel.rankImage = R.drawable.logomark_navy
        listCurrent.add(currentModel)

        for (i in 0 until ranks.size - 1) {
            val model = RankModel()
            model.rankImage = ranks[i]
            model.rankText = rankstext[i]
            listCurrent.add(model)
        }

        spnCurrentRank.adapter = RankAdapter(this, listCurrent)
        spnCurrentRank.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                listWanted.clear()
                if (p2 == 0) {
                    val wantedModel = RankModel()
                    wantedModel.rankImage = R.drawable.logomark_navy
                    wantedModel.rankText = "WANTED RANK"
                    listWanted.add(wantedModel)
                }

                for (i in spnCurrentRank.selectedItemPosition until spnCurrentRank.selectedItemPosition + 5) {
                    if (i < ranks.size) {
                        val model = RankModel()
                        model.rankImage = ranks[i]
                        model.rankText = rankstext[i]
                        listWanted.add(model)
                    }
                }

                spnWantedRank.adapter = RankAdapter(this@NewOrderActivity, listWanted)

                if (spnCurrentRank.selectedItemPosition != 0) {
                    val currentRank = listCurrent[spnCurrentRank.selectedItemPosition].rankText
                    val wantedRank = listWanted[spnWantedRank.selectedItemPosition].rankText
                    var c = 0
                    var w = 0

                    for (i in rankstext.indices) {
                        if (currentRank == rankstext[i]) {
                            c = i
                        }
                        if (wantedRank == rankstext[i]) {
                            w = i
                        }
                    }
                    val subTotal = "IDR ${((w - c) * 20)}.000,-"
                    tvPrice.text = subTotal
                }
                spnWantedRank.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (spnCurrentRank.selectedItemPosition != 0) {
                            val currentRank =
                                listCurrent[spnCurrentRank.selectedItemPosition].rankText
                            val wantedRank = listWanted[spnWantedRank.selectedItemPosition].rankText
                            var c = 0
                            var w = 0

                            for (i in rankstext.indices) {
                                if (currentRank == rankstext[i]) {
                                    c = i
                                }
                                if (wantedRank == rankstext[i]) {
                                    w = i
                                }
                            }
                            val subTotal = "IDR ${((w - c) * 20)}.000,-"
                            tvPrice.text = subTotal


                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}