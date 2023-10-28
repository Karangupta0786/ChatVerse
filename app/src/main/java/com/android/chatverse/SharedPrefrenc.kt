package com.android.chatverse
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
class SharedPreferencesUtil(context: Context) {

    // SharedPreferences instance
    private val sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

    // Editor to make changes
    private val editor = sharedPreferences.edit()

    fun saveURI(uri:String){
        editor.putString("uri",uri)
        editor.apply()
    }

    // Function to save data
    fun saveData(uid: String, address: String, token: String,userId:String) {
        editor.putString("uid", uid)
        editor.putString("address", address)
        editor.putString("token", token)
        editor.putString("userId",userId)
        editor.apply()
    }
    fun saveFlagValue(login:Boolean){
        editor.putBoolean("login",login)
        editor.apply()
    }

    // Function to retrieve data
    fun retrieveData(): Pair<String, Triple<String, String, String>> {
        val savedUid = sharedPreferences.getString("uid", "") ?: ""
        val savedAddress = sharedPreferences.getString("address", "") ?: ""
        val savedToken = sharedPreferences.getString("token", "") ?: ""
        val savedUserId = sharedPreferences.getString("userId", "") ?: ""

        val dataTriple = Triple(savedUid, savedAddress, savedToken)
        return Pair(savedUserId, dataTriple)
    }

    fun retrieveURI(): Uri {
        val uriString = sharedPreferences.getString("uri", "")
        return if (uriString.isNullOrEmpty()) {
            Uri.EMPTY // or you can provide a default Uri if needed
        } else {
            Uri.parse(uriString)
        }
    }
    fun getFlagValue(): Boolean {
        return sharedPreferences.getBoolean("login", false)
    }

}

