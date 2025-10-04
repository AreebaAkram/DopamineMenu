package com.example.dopaminemenu.vibemenu.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.dopaminemenu.databinding.UserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class UserProfile : AppCompatActivity() {

    private lateinit var binding: UserProfileBinding
    private var imageUri: Uri? = null
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "test_user"
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load profile if exists
        loadProfileImage()

        binding.editIcon.setOnClickListener {
            pickImage()
        }
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                imageUri = result.data!!.data
                val bitmap = uriToBitmap(imageUri!!)
                binding.profileImage.setImageBitmap(bitmap)

                val base64Image = bitmapToBase64(bitmap)
                saveImageToRealtimeDB(base64Image)
            }
        }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    // Convert Uri → Bitmap
    private fun uriToBitmap(uri: Uri): Bitmap {
        val inputStream = contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

    // Convert Bitmap → Base64 String
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Convert Base64 → Bitmap
    private fun base64ToBitmap(base64Str: String): Bitmap {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    // Save Base64 image string into Firebase Realtime Database
    private fun saveImageToRealtimeDB(base64Image: String) {
        dbRef.child(userId).child("profileImage").setValue(base64Image)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile image updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Load image from Firebase when opening profile
    private fun loadProfileImage() {
        dbRef.child(userId).child("profileImage").get()
            .addOnSuccessListener { snapshot ->
                val base64Image = snapshot.getValue(String::class.java)
                if (!base64Image.isNullOrEmpty()) {
                    val bitmap = base64ToBitmap(base64Image)
                    binding.profileImage.setImageBitmap(bitmap)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load profile: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
