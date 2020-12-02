package com.application.ravenrankboost.Helper

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.application.ravenrankboost.View.SplashScreenActivity

class SessionManager(context: Context) {
    val context: Context
    val pref: SharedPreferences
    val editor: SharedPreferences.Editor
    val PRIVATEMODE = 0

    private var instance: SessionManager? = null

    init {
        this.context = context
        pref = this.context.getSharedPreferences("PREFVALO", PRIVATEMODE)
        editor = pref.edit()
    }

    fun getInstance(context: Context): SessionManager {
        if (instance == null) {
            instance = SessionManager(context)
        }
        return instance!!
    }

    fun createSession(
        email: String,
        username: String,
        memberType: String,
        since:String
    ) {
        editor.putString("EMAIL", email)
        editor.putString("USERNAME", username)
        editor.putString("TYPE", memberType)
        editor.putString("SINCE",since)
        if (memberType == "Admin") {
            editor.putBoolean("isAdmin", true)
        } else {
            editor.putBoolean("isAdmin", false)
        }
        editor.commit()
    }

    fun setIsLogin(){
        editor.putBoolean("isLogin", true)
        editor.commit()
    }

    fun getStringFromSP(string: String): String? {
        return pref.getString(string, "")
    }

    fun isAdmin(): Boolean {
        return pref.getBoolean("isAdmin", false)
    }

    fun endSessionLogout() {
        val pref = context.getSharedPreferences("PREFVALO", PRIVATEMODE)
        val editor = pref.edit()
        editor.clear()
        editor.commit()

        val intent = Intent(context, SplashScreenActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean("isLogin", false)
    }

}