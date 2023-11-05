package com.android.chatverse

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.chatverse.DataClasses.UserProfile
import com.android.chatverse.MessagingModule.UserListFragment
import com.android.chatverse.databinding.ActivityDashboardScreenBinding
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardScreen : AppCompatActivity(),OnNavigationItemSelectedListener {

    lateinit var binding: ActivityDashboardScreenBinding
    lateinit var imgProfile:ImageView
    lateinit var name:TextView
    lateinit var age:TextView
    lateinit var gender:TextView

    var actionBarDrawerToggle:ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashboardScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val uri = SharedPreferencesUtil(applicationContext).retrieveURI()
        val toastMessage = "$uri"

        Toast.makeText(applicationContext,  toastMessage, Toast.LENGTH_SHORT).show()

//        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
//        val x = sharedPreferences.getString("name","karan").toString()
//        val y = sharedPreferences.getString("age","20").toString()
//        Toast.makeText(applicationContext, "Name is $x", Toast.LENGTH_SHORT).show()
//        Toast.makeText(applicationContext, "Age is $y", Toast.LENGTH_SHORT).show()




        actionBarDrawerToggle = ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)

        binding.navigationView.setNavigationItemSelectedListener(this)

        binding.imgNav.setOnClickListener {
                binding.drawerLayout.openDrawer(GravityCompat.START)
    }

        val header = LayoutInflater.from(this).inflate(R.layout.header_layout,binding.navigationView,false)
        binding.navigationView.addHeaderView(header)

        name = header.findViewById(R.id.name)
        imgProfile = header.findViewById(R.id.img_profile)
        age = header.findViewById(R.id.age)
        gender = header.findViewById(R.id.gender)

        loadUserProfile()
        val edit = header.findViewById<ImageView>(R.id.edit)
        edit.setOnClickListener {

            // Assuming you have a reference to the application context
            val sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)

            // Retrieve saved data
            val (uid, address, token) = sharedPreferencesUtil.retrieveData().second

            // Create a toast message with the retrieved data
            val toastMessage = "UID: $uid\nAddress: $address\nToken: $token"
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()

//            binding.imgNav.setOnClickListener {
//            }


//            val fragmentManager = supportFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.frameLayout,EditprofileFragment())
//            fragmentTransaction.commit()
            startActivity(Intent(this,EditProfileActivity::class.java))
            binding.drawerLayout.closeDrawer(GravityCompat.START)

            
        }


        binding.llGender.setOnClickListener {
            Toast.makeText(applicationContext, "Looking for gender", Toast.LENGTH_SHORT).show()
            val dialog =
                Dialog(this) // Use 'this' as the context, assuming this code is within an Activity
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_gender_selection)
            dialog.window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
                setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.gravity = Gravity.CENTER

            dialog.window?.attributes = layoutParams

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.END)

            dialog.show()
            val male = dialog.findViewById<RadioButton>(R.id.dialog_male)
            val female = dialog.findViewById<RadioButton>(R.id.dialog_female)
            male.setOnClickListener {
                male.isChecked = true
                female.isChecked = false
            }
            female.setOnClickListener {
                female.isChecked = true
                male.isChecked = false
            }
        }
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,UserListFragment())
        fragmentTransaction.commit()
//        binding.drawerLayout.closeDrawer(GravityCompat.START)
}


    private fun loadUserProfile() {
            val userId = SharedPreferencesUtil(applicationContext).retrieveData().first

            val call = RetrofitObject.retroit.getUserProfile(userId)
            call.enqueue(object : Callback<UserProfile?> {
                override fun onResponse(
                    call: Call<UserProfile?>,
                    response: Response<UserProfile?>
                ) {
                    if (response.isSuccessful){
                        val userProfile = response.body()

                        Toast.makeText(applicationContext, "load user profile", Toast.LENGTH_SHORT).show()

                        name.setText(userProfile?.name.toString())
                        age.setText(userProfile?.age)
                        gender.setText(userProfile?.gender)
                        Toast.makeText(applicationContext, "${userProfile?.gender.toString()}", Toast.LENGTH_SHORT).show()
                        Toast.makeText(applicationContext, userProfile?.lookingForGender.toString(), Toast.LENGTH_SHORT).show()
//                    if ( userProfile?.gender){
//                        binding.imgBoy.strokeWidth = 0
//                        binding.imgGirl.strokeWidth = 5
//                    }
//                    else{
//                        binding.imgBoy.strokeWidth = 5
//                        binding.imgGirl.strokeWidth = 0
//                    }

                        Glide.with(this@DashboardScreen)
                            .load(userProfile?.profilePic)
                            .into(imgProfile)
                    }
                }

                override fun onFailure(call: Call<UserProfile?>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        
        
        
        when(item.itemId){
            R.id.a -> {
                Toast.makeText(applicationContext, "a", Toast.LENGTH_SHORT).show()
            }
            R.id.b -> {
                Toast.makeText(applicationContext, "b", Toast.LENGTH_SHORT).show()
            }
            R.id.c -> {
                Toast.makeText(applicationContext, "c", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}