package com.android.chatverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
//        val x = SharedPreferencesUtil(applicationContext)

        // Assuming you have a reference to the application context
        val sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)

        // Retrieve saved data
        val (uid, address, token) = sharedPreferencesUtil.retrieveData().second

        // Create a toast message with the retrieved data
        val toastMessage = "UID: $uid\nAddress: $address\nToken: $token"
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()

    }
}