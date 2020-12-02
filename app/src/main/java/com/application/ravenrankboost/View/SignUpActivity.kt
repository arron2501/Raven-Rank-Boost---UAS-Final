package com.application.ravenrankboost.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.application.ravenrankboost.Databases.DatabaseLoginHelper
import com.application.ravenrankboost.R
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {
    lateinit var db: DatabaseLoginHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        db = DatabaseLoginHelper(this)

        tvBackToLogin.setOnClickListener { view ->
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
            finish()
        }

        btSignUp.setOnClickListener { view ->
            if (validate()) {
                val email = etEmail.text.toString().trim()
                val username = etUsername.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val monthNames = arrayOf(
                    "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                    "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
                )
                val date = Date()
                val year = SimpleDateFormat("yyyy", Locale.US).format(Date())
                val since = "${date.date} ${monthNames[date.month]} $year"

                var isExist = false

                val cursor = db.allData
                while (cursor.moveToNext()) {
                    if (cursor.getString(0) == email || cursor.getString(1) == username) {
                        isExist = true
                    }
                }
                if (!isExist) {
                    if (db.insertdata(email, username, password, since)) {
                        toast("Success")
                        Handler().postDelayed({
                            startActivity(Intent(this, SignInActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            })
                            finish()
                        }, 1000)
                    } else {
                        toast("Failed\nEmail/Username sudah digunakan")
                    }
                } else {
                    toast("Failed\nEmail/Username sudah digunakan")
                }
            }
        }
    }

    fun validate(): Boolean {
        var valid = true
        if (etEmail.text.toString().trim().isBlank()) valid = false
        if (etUsername.text.toString().trim().isBlank()) valid = false
        if (etPassword.text.toString().trim().isBlank()) valid = false
        return valid
    }
}