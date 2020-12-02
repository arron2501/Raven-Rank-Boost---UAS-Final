package com.application.ravenrankboost.View

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.application.ravenrankboost.Databases.DatabaseLoginHelper
import com.application.ravenrankboost.Helper.SessionManager
import com.application.ravenrankboost.R
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.toast
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var db: DatabaseLoginHelper
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        sessionManager = SessionManager(this)
        db = DatabaseLoginHelper(this)
        btHelp.setOnClickListener(this)
        btBack.setOnClickListener(this)
        imgProfile.setOnClickListener(this)
        btSignOut.setOnClickListener(this)
        tvName.text = sessionManager.getStringFromSP("USERNAME")
        tvDate.text = sessionManager.getStringFromSP("SINCE")
        tvEmail.text = sessionManager.getStringFromSP("EMAIL")
        setUpProfilePicture()
    }

    fun setUpProfilePicture() {
        val cursor = db.allData
        while (cursor.moveToNext()) {
            if (cursor.getString(0) == sessionManager.getStringFromSP("EMAIL") && cursor.getString(1) == sessionManager.getStringFromSP(
                    "USERNAME"
                )
            ) {
                val profileImage = cursor.getBlob(4)
                if (profileImage == null || profileImage.isEmpty()) {
                    imgProfile.setImageResource(R.drawable.profilimage)
                } else {
                    val outImage: ByteArray = profileImage
                    val imageStream = ByteArrayInputStream(outImage)
                    val theImage = BitmapFactory.decodeStream(imageStream)
                    imgProfile.setImageBitmap(theImage)
                }
            }
        }
    }

    fun checkWriteExternalPermission(): Boolean {
        val permission: String = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val res: Int = this.checkCallingOrSelfPermission(permission)
        return (res == PackageManager.PERMISSION_GRANTED)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btHelp -> startActivity(
                Intent(
                    this@ProfileActivity,
                    ContactCenterActivity::class.java
                )
            )
            R.id.btBack -> onBackPressed()
            R.id.imgProfile -> {
                if (checkWriteExternalPermission()) {
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    intent.type = "image/*"
                    startActivityForResult(Intent.createChooser(intent, "Select File"), 10)
                } else {
                    ActivityCompat.requestPermissions(
                        this@ProfileActivity,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        10
                    )
                }
            }
            R.id.tos -> startActivity(
                Intent(
                    this@ProfileActivity,
                    TermsOfServiceActivity::class.java
                )
            )
            R.id.privacy -> startActivity(
                Intent(
                    this@ProfileActivity,
                    PrivacyNoticeActivity::class.java
                )
            )
            R.id.btSignOut -> sessionManager.endSessionLogout()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select File"), 10)
        } else {
            toast("Harap berikan permission terlebih dahulu")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 10) {
                val uri = data!!.data
                var cursor: Cursor? = null
                val path: String

                try {
                    val proj = arrayOf(MediaStore.Images.Media.DATA)
                    cursor = contentResolver.query(uri!!, proj, null, null, null)
                    val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor?.moveToFirst()
                    path = column_index?.let { cursor?.getString(it) }.toString()
                } finally {
                    cursor?.close()
                }
                val bmpProfilePicture = BitmapFactory.decodeFile(path)
                val stream = ByteArrayOutputStream()
                bmpProfilePicture.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                // val imageByte = Base64.encode(stream.toByteArray(), Base64.NO_WRAP)
                val imageByte = stream.toByteArray()
                db.updateProfileImage(sessionManager.getStringFromSP("EMAIL"), imageByte)
                setUpProfilePicture()
            }
        }
    }
}