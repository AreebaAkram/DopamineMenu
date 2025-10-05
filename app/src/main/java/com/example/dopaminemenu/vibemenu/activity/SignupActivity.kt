package com.example.dopaminemenu.vibemenu.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dopaminemenu.databinding.SignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    var binding: SignupBinding? = null
    var firebaseauth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseauth = FirebaseAuth.getInstance()

        binding?.h1?.setText("Create Account")
        binding?.emailLabel?.setText("Email")
        binding?.passwordLabel?.setText("Password")
        binding?.confirmPasswordLabel?.setText("Confirm Password")
        binding?.btnSignup?.setText("Sign Up")

        binding?.loginbtn?.setText("Already have an account? Login")

        binding?.loginbtn?.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding?.btnSignup?.setOnClickListener() {

            val email = binding?.edtEmail?.text.toString()
            val password = binding?.edtPassword?.text.toString()
            val confirmPassword = binding?.edtConfirmPassword?.text.toString()



            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {

                if (password == confirmPassword) {
                    firebaseauth?.createUserWithEmailAndPassword(
                        binding?.edtEmail?.text.toString(),
                        binding?.edtPassword?.text.toString()
                    )?.addOnCompleteListener() {
                        if (it.isSuccessful) {
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password not matches", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }

        }
    }
}