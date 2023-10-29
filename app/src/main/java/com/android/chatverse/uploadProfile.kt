package com.android.chatverse

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.android.chatverse.databinding.ActivityUploadProfileBinding
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class uploadProfile : AppCompatActivity() {
    lateinit var binding:ActivityUploadProfileBinding
    private val PICK_IMAGE_FROM_GALLERY = 1
    private val TAKE_PICTURE = 2
    private val CAMERA_PERMISSION_CODE = 101
    private var selectedImageUri = ""
    lateinit var imageView: ImageView
    lateinit var editImage:ImageView
    lateinit var userName:String
    lateinit var userAge:String
    lateinit var imgUri: Uri
    lateinit var maleButton: RadioButton
    lateinit var femaleButton: RadioButton
    private var gender = "female"
//    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()){
////        imgUri = null!!
//        imageView.setImageURI(null)
//        imageView.setImageURI(it)
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_profile)
        imageView = findViewById(R.id.img_upload)
        editImage = findViewById(R.id.edt_img)
    maleButton = findViewById(R.id.maleButton)
    femaleButton = findViewById(R.id.femaleButton)

    maleButton.setOnClickListener {
        maleButton.isChecked = true
        gender = "male"
        femaleButton.isChecked = false
    }
    femaleButton.setOnClickListener {
        femaleButton.isChecked = true
        maleButton.isChecked = false
        gender = "female"
    }


    userName = findViewById<TextInputEditText>(R.id.user_name).text.toString()
    userAge = findViewById<TextInputEditText>(R.id.user_age).text.toString()

        editImage.setOnClickListener {
            checkPermissionAndShowDialog()
        }

        findViewById<CardView>(R.id.btn_submit).setOnClickListener {
            val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)

            setUpProfile()

            Toast.makeText(applicationContext, "$gender", Toast.LENGTH_SHORT).show()

//            startActivity(Intent(this@uploadProfile,DashboardScreen::class.java))
        }
    }
//    private fun createUri(): Uri? {
//        val image = File(applicationContext.filesDir,"camera_photo.png")
//        return FileProvider.getUriForFile(applicationContext,"com.android.chatverse.fileProvider",image)
//    }

//    private fun setUpProfile(){
//    }
    private fun checkPermissionAndShowDialog() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PERMISSION_GRANTED
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
                if (grantResults.isNotEmpty() && grantResults[0] ==  PERMISSION_GRANTED) {
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

        AlertDialog.Builder(this@uploadProfile)
            .setTitle("Choose an option")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        val galleryIntent =
                            Intent(
                               ACTION_PICK,
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
                    imageView.setImageURI(Uri.parse(selectedImageUri))

                }
                TAKE_PICTURE -> {
                    // User took a picture using the camera
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    // Handle the captured image, for example, load it into an ImageView
                    imageView.setImageBitmap(imageBitmap)
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

// Assuming you have a File or Uri for the image
        val imageFile = File(getRealPathFromUri(imgUri))  // You need to implement getRealPathFromUri function

// Create a RequestBody from the file
        val imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)

// Create a MultipartBody.Part from the RequestBody
        val profilePicPart = MultipartBody.Part.createFormData("profilePic", imageFile.name, imageRequestBody)

        val send = RetrofitObject.retroit.updateProfile(
            userId,name,age,gender,profilePicPart
        )

        send.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(applicationContext,response.message.toString(),Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@uploadProfile,DashboardScreen::class.java))
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