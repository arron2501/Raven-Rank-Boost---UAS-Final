package com.application.ravenrankboost.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.ravenrankboost.R
import kotlinx.android.synthetic.main.activity_contact_center.*

class ContactCenterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_center)

        btBack.setOnClickListener { view ->
            onBackPressed()
        }

        btEmail.setOnClickListener{ view ->
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("arron2501@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Center - Raven Rank Boost")
            intent.putExtra(Intent.EXTRA_TEXT, "I need help about something...")

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        btWA.setOnClickListener { view ->
            val url = "https://wa.me/+6285777709900?text=I%20need%20help%20about%20something..."
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}