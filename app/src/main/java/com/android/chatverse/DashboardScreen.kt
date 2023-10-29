package com.android.chatverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.chatverse.MessagingModule.UserListFragment
import com.android.chatverse.databinding.ActivityDashboardScreenBinding
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class DashboardScreen : AppCompatActivity(),OnNavigationItemSelectedListener {

    lateinit var binding: ActivityDashboardScreenBinding

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


            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout,EditprofileFragment())
            fragmentTransaction.commit()
            binding.drawerLayout.closeDrawer(GravityCompat.START)

        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,UserListFragment())
        fragmentTransaction.commit()
        binding.drawerLayout.closeDrawer(GravityCompat.START)
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