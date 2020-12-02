package com.application.ravenrankboost.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.application.ravenrankboost.Databases.DatabaseLoginHelper
import com.application.ravenrankboost.Helper.SessionManager
import com.application.ravenrankboost.View.Dashboard.DashboardActivity
import com.application.ravenrankboost.R
import com.application.ravenrankboost.View.ListOrder.ListOrderActivity
import kotlinx.android.synthetic.main.activity_signin.*
import org.jetbrains.anko.toast

class SignInActivity : AppCompatActivity() {
    lateinit var db: DatabaseLoginHelper
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        db = DatabaseLoginHelper(this)
        sessionManager = SessionManager(this)

        tvCreateAccount.setOnClickListener { v ->
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }
        btSignIn.setOnClickListener(View.OnClickListener { view ->
            var email = ""
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            var since = ""

            if (username == "asvian" || username == "andre" || username == "rival" && password == "admin") {
                email = "admin"
                sessionManager.createSession(email, password, "Admin", "25 NOV 2020")
                if (cbStay.isChecked) {
                    sessionManager.setIsLogin()
                }
                startActivity(Intent(this@SignInActivity, ListOrderActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
                finish()
            } else {
                var isExist = false

                val cursor = db.allData
                while (cursor.moveToNext()) {
                    if (cursor.getString(1) == username && cursor.getString(2) == password) {
                        isExist = true
                        email = cursor.getString(0)
                        since = cursor.getString(3)
                    }
                }
                if (isExist) {
                    sessionManager.createSession(email, username, "Member", since)
                    if (cbStay.isChecked) {
                        sessionManager.setIsLogin()
                    }
                    startActivity(Intent(this@SignInActivity, DashboardActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    })
                    finish()
                } else {
                    toast("Failed\nUsername/Password salah")
                }
            }
        })
        tvStay.setOnClickListener { view ->
            cbStay.isChecked = !cbStay.isChecked
        }
    }
}