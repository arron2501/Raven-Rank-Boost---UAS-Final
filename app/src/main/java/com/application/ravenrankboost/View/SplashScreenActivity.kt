package com.application.ravenrankboost.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import com.application.ravenrankboost.Helper.SessionManager
import com.application.ravenrankboost.R
import com.application.ravenrankboost.View.Dashboard.DashboardActivity
import com.application.ravenrankboost.View.ListOrder.ListOrderActivity

class SplashScreenActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        sessionManager = SessionManager(this)

        Handler().postDelayed({
            if (sessionManager.isLoggedIn()) {
                if (sessionManager.isAdmin()) {
                    startActivity(Intent(this, ListOrderActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    })
                } else {
                    startActivity(Intent(this, DashboardActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    })
                }
                finish()
            } else {
                startActivity(Intent(this, SignInActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
                finish()
            }
        }, 2500)
    }
}