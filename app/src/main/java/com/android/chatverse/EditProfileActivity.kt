package com.android.chatverse

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.chatverse.DataClasses.UserProfile
import com.android.chatverse.databinding.ActivityEditprofileBinding
import com.bumptech.glide.Glide
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    private val PICK_IMAGE_FROM_GALLERY = 1
    private val TAKE_PICTURE = 2
    private val CAMERA_PERMISSION_CODE = 101

    private var selectedImageUri = ""
    lateinit var userName:String
    lateinit var userAge:String
    lateinit var imgUri: Uri
    lateinit var gender:String
    private var lookingFor = "F"
    var x = true

    lateinit var binding:ActivityEditprofileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.imgBoy.setOnClickListener {
            gender = "Male"
            binding.imgBoy.strokeWidth = 5
            binding.imgGirl.strokeWidth = 0
        }
        binding.imgGirl.setOnClickListener {
            gender = "Female"
            binding.imgBoy.strokeWidth = 0
            binding.imgGirl.strokeWidth = 5
        }
        loadUserProfile()
//        if (SharedPreferencesUtil(applicationContext).getFlagValue()){
//        }

        Toast.makeText(applicationContext, SharedPreferencesUtil(applicationContext).retrieveData().second.second, Toast.LENGTH_SHORT).show()

        // Set a click listener for your ImageView or any other view you want to use to trigger image selection
        binding.editImg.setOnClickListener {
            checkPermissionAndShowDialog()
        }
        binding.btnSubmit.setOnClickListener {
            userName = binding.etName.text.toString()
            userAge = binding.etAge.text.toString()

            setUpProfile()
        }

    }
        private fun loadUserProfile() {
        val userId = SharedPreferencesUtil(applicationContext).retrieveData().first

        val call = RetrofitObject.retroit.getUserProfile(userId)
        call.enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()

//                    Toast.makeText(applicationContext, "load user profile", Toast.LENGTH_SHORT).show()

                    binding.etName.setText(userProfile?.name.toString())
                    binding.etAge.setText(userProfile?.age)
//                    Toast.makeText(applicationContext, "${userProfile?.gender.toString()}", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(applicationContext, userProfile?.lookingForGender.toString(), Toast.LENGTH_SHORT).show()
                    if ( userProfile?.gender == "Female"){
                        binding.imgBoy.strokeWidth = 0
                        binding.imgGirl.strokeWidth = 5
                    }
                    else{
                        binding.imgBoy.strokeWidth = 5
                        binding.imgGirl.strokeWidth = 0
                    }

                    Glide.with(this@EditProfileActivity)
                        .load(userProfile?.profilePic)
                        .into(binding.imgUser)
                }
                else{
                    Log.e("response",response.message().toString())
                    Log.e("response", "Error Code: ${response.code()}, Message: ${response.message()}")
                    Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                Log.e("failure",t.localizedMessage.toString())
            }
        })
    }

    private fun checkPermissionAndShowDialog() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Camera permission is granted, show the image picker dialog
            showImagePickerDialog()
        } else {
            // Request camera permission
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf( Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission granted, show the image picker dialog
                    showImagePickerDialog()
                } else {
                    // Camera permission denied
                    // Handle the case where the user denied the camera permission
                }
                return
            }
            else -> {
                // Handle other permission requests if needed
            }
        }
    }

    private fun showImagePickerDialog() {
        // Create a dialog or use any UI element to allow the user to choose between camera and gallery

        // For simplicity, let's use a simple dialog with two options
        val options = arrayOf("Gallery", "Camera")

        AlertDialog.Builder(this@EditProfileActivity)
            .setTitle("Choose an option")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        val galleryIntent =
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                        startActivityForResult(galleryIntent, PICK_IMAGE_FROM_GALLERY)
                    }
                    1 -> {
                        takePicture()
                    }
                }
            }
            .show()
    }

    private fun takePicture() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, TAKE_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_FROM_GALLERY -> {
                    // User selected an image from the gallery
                    imgUri = data?.data!!
                    selectedImageUri = data?.data.toString()
                    val x = SharedPreferencesUtil(applicationContext)
                    x.saveURI(selectedImageUri.toString())
                    // Handle the selected image URI, for example, load it into an ImageView
                    binding.imgUser.setImageURI(Uri.parse(selectedImageUri))

                }
                TAKE_PICTURE -> {
                    // User took a picture using the camera
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    // Handle the captured image, for example, load it into an ImageView
                    binding.imgUser.setImageBitmap(imageBitmap)
                }
            }
        }
    }

    private fun setUpProfile(){

        val data = SharedPreferencesUtil(applicationContext).retrieveData()
        val userId = data.first
        Log.e("userId",userId.toString())
        val name = RequestBody.create(MultipartBody.FORM, userName)
        val age = RequestBody.create(MultipartBody.FORM, userAge)
        val gender = RequestBody.create(MultipartBody.FORM, gender)
        val lookingFor = RequestBody.create(MultipartBody.FORM,lookingFor)

// Assuming you have a File or Uri for the image
        val imageFile = File(getRealPathFromUri(imgUri))  // You need to implement getRealPathFromUri function

// Create a RequestBody from the file
        val imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)

// Create a MultipartBody.Part from the RequestBody
        val profilePicPart = MultipartBody.Part.createFormData("profilePic", imageFile.name, imageRequestBody)

        val send = RetrofitObject.retroit.updateProfile(
            name,profilePicPart,age,gender,lookingFor,userId
        )
        send.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(applicationContext,response.message.toString(),Toast.LENGTH_LONG).show()
                    Toast.makeText(applicationContext, "set up profile", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@EditProfileActivity,DashboardScreen::class.java))
                    finish()
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }
    private fun getRealPathFromUri(uri: Uri): String? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = cursor?.getString(columnIndex!!)
        cursor?.close()
        return filePath
    }

}