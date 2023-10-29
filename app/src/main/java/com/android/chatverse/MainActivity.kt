package com.android.chatverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.chatverse.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var email = ""
    var password = ""
    var confirmPassword = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        confirmPassword = binding.etPswrd2.text.toString()
        email = binding.etEmail.text.toString()
        password = binding.etPswrd.text.toString()

        binding.continueCard.setOnClickListener {
            val enteredPassword = binding.etPswrd.text.toString()
            val enteredConfirmPassword = binding.etPswrd2.text.toString()

            if (enteredPassword != enteredConfirmPassword) {
                binding.passwordBox2.error = "Passwords do not match"
                binding.passwordBox2.isErrorEnabled = true
            } else {
                binding.passwordBox2.isErrorEnabled = false
                Toast.makeText(applicationContext, "Welcome to ChatVerse", Toast.LENGTH_LONG).show()
                 binding.progressCircular.isVisible = true
                 getSignup()
            }
        }



        binding.imgBack.setOnClickListener { onBackPressed() }

        binding.goToLogIn.setOnClickListener {
            startActivity(Intent(this@MainActivity,LoginScreen::class.java))
            finish()
        }


    }

    private fun getSignup() {
        val apiClient = RetrofitObject.retroit
        val signUpRequest = SignUpData("$email","$password")
        val call = apiClient.signup(signUpRequest)
        call.enqueue(object : Callback<signUpResponse?> {
            override fun onResponse(call: Call<signUpResponse?>, response: Response<signUpResponse?>) {
                binding.progressCircular.isVisible = false
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity,LoginScreen::class.java))
                }
                else{
                    Toast.makeText(applicationContext, "Email allready exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<signUpResponse?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}