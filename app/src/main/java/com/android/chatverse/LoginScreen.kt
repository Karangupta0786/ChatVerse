package com.android.chatverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import com.android.chatverse.databinding.ActivityLoginScreenBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginScreen : AppCompatActivity() {
    lateinit var binding: ActivityLoginScreenBinding
    var Email = ""
    var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.goToSignUp.setOnClickListener {
            startActivity(Intent(this@LoginScreen,MainActivity::class.java))
            finish()
        }
        binding.imgBack.setOnClickListener { onBackPressed() }

        binding.loginCard.setOnClickListener {
            getLogin()
        }




    }

    private fun getLogin() {
        Email = binding.loginEmail.text.toString()
        password = binding.loginPassword.text.toString()

//        val x = "karanji@gamil.com"
//        val y = "12345678"

//        Toast.makeText(applicationContext, Email, Toast.LENGTH_SHORT).show()
//        Toast.makeText(applicationContext, password, Toast.LENGTH_SHORT).show()
//        if (Email=="karanji@gamil.com" && password=="12345678"){
//            Toast.makeText(applicationContext, "correct details", Toast.LENGTH_SHORT).show()
//    }
//        else{
//            Toast.makeText(applicationContext, "inCorrect details", Toast.LENGTH_SHORT).show()
//        }
        val apiClient = retrofitObject.retroit
        val loginRequest = LoginRequest( Email, password)
        val call = apiClient.login(loginRequest)

        call.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    Toast.makeText(
                        applicationContext,
                        "Congratulations!! Login SuccessFull",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.e("error",response.toString())
                    SharedPreferencesUtil(applicationContext).saveData(response?.mesiboDetails?.uid.toString(),response?.mesiboDetails?.token.toString(),response?.mesiboDetails?.address.toString(),response?.userId.toString())

                    SharedPreferencesUtil(applicationContext).saveFlagValue(true)

                    startActivity(Intent(this@LoginScreen,uploadProfile::class.java))
                } else {
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT)
                        .show()
                    Toast.makeText(applicationContext, response.body()?.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })


//        if (email=="karanji@gamil.com"&&password=="12345678"){
//    }
//        else{
//            Toast.makeText(applicationContext, "Incorrect Details", Toast.LENGTH_SHORT).show()
//        }
    }
}