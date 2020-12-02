package com.application.ravenrankboost.View.Notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.application.ravenrankboost.Databases.DatabaseNotifHelper
import com.application.ravenrankboost.Helper.SessionManager
import com.application.ravenrankboost.R
import kotlinx.android.synthetic.main.activity_notification_date.*

class NotificationActivity : AppCompatActivity() {
    lateinit var list: ArrayList<NotificationModel>
    lateinit var tmplist: ArrayList<NotificationModel>
    lateinit var db: DatabaseNotifHelper
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_date)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        sessionManager = SessionManager(this)
        db = DatabaseNotifHelper(this)
        list = ArrayList()
        tmplist = ArrayList()
        btBack.setOnClickListener { view ->
            onBackPressed()
        }
        var tmpDate = ""
        val cursor = db.allData
        cursor.moveToLast()
        do {
            if(cursor.count > 0){
                if (cursor.getString(1) == sessionManager.getStringFromSP("EMAIL")) {
                    if (tmpDate != cursor.getString(2)) {
                        val model = NotificationModel()
                        tmpDate = cursor.getString(2)
                        model.type = "date"
                        model.text = cursor.getString(2)
                        list.add(model)
                    }
                    val model = NotificationModel()
                    model.type = "notif"
                    model.text = cursor.getString(3)
                    list.add(model)
                }
            }
        } while (cursor.moveToPrevious())
        val adapter = NotificationAdapter(this@NotificationActivity, list)
        lvNotification.adapter = adapter
    }
}