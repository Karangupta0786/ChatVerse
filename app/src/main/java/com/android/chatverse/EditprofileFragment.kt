package com.android.chatverse
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.android.chatverse.databinding.FragmentEditprofileBinding
import com.bumptech.glide.Glide


class EditprofileFragment : Fragment() {
    private val PICK_IMAGE_FROM_GALLERY = 1
    private val TAKE_PICTURE = 2
    private val CAMERA_PERMISSION_CODE = 101

    lateinit var binding:FragmentEditprofileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditprofileBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgBoy.setOnClickListener {
            binding.imgBoy.strokeWidth = 5
            binding.imgGirl.strokeWidth = 0
        }
        binding.imgGirl.setOnClickListener {
            binding.imgBoy.strokeWidth = 0
            binding.imgGirl.strokeWidth = 5
        }


            // Set a click listener for your ImageView or any other view you want to use to trigger image selection
        binding.editImg.setOnClickListener {
            checkPermissionAndShowDialog()
        }

    }
    private fun checkPermissionAndShowDialog() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
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
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
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

        AlertDialog.Builder(requireContext())
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

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_FROM_GALLERY -> {
                    // User selected an image from the gallery
                    val selectedImageUri = data?.data
                    // Handle the selected image URI, for example, load it into an ImageView
                    binding.imgUser.setImageURI(selectedImageUri)

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

}