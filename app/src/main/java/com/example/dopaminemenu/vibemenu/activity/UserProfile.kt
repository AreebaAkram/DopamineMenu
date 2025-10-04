package com.example.dopaminemenu.vibemenu.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.dopaminemenu.R
import com.example.dopaminemenu.databinding.UserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.ByteArrayOutputStream

class UserProfile : AppCompatActivity() {

    private lateinit var binding: UserProfileBinding
    private var imageUri: Uri? = null
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "test_user"
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var completed: TextView
    private lateinit var pending: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadProfileImage()

        binding.editIcon.setOnClickListener {
            pickImage()
        }

        binding.tasks.setText("Tasks Overview")
        binding.statsview.completed.setText("Completed")
        binding.statsview.pending.setText("Pending")
        completed = binding.statsview.numcompleted
        pending = binding.statsview.numpending
        loadStats()


        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_add -> {
                    val intent = Intent(this, AddActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_profile -> {
                    val intent = Intent(this, UserProfile::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
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
                Toast.makeText(this, "Failed to load profile: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun loadStats() {
        val activitiesRef = FirebaseDatabase.getInstance().getReference("activities")

        activitiesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var completedCount = 0
                var pendingCount = 0

                for (activitySnapshot in snapshot.children) {
                    val state = activitySnapshot.child("state").getValue(String::class.java)
                    if (state == "completed") {
                        completedCount++
                    } else if (state == "pending") {
                        pendingCount++
                    }
                }

                completed.text = completedCount.toString()
                pending.text = pendingCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UserProfile, "Error: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


}
