package com.example.dopaminemenu.vibemenu.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dopaminemenu.MainActivity
import com.example.dopaminemenu.databinding.LoginBinding
import com.example.dopaminemenu.vibemenu.home.HomeActivity
import com.example.dopaminemenu.vibemenu.signin.SignupActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity() : AppCompatActivity() {

    var binding: LoginBinding? = null
    var firebaseauth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseauth = FirebaseAuth.getInstance()

        binding?.h1?.setText("Login")
        binding?.emailLabel?.setText("Email")
        binding?.passwordLabel?.setText("Password")
        binding?.btnLogin?.setText("Login")
        binding?.signupbtn?.setText("Create Account")

        binding?.signupbtn?.setOnClickListener() {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }


        binding?.btnLogin?.setOnClickListener() {

            val email = binding?.edtEmail?.text.toString()
            val password = binding?.edtPassword?.text.toString()



            if (email.isNotEmpty() && password.isNotEmpty()) {


                firebaseauth?.signInWithEmailAndPassword(
                    binding?.edtEmail?.text.toString(),
                    binding?.edtPassword?.text.toString()
                )?.addOnCompleteListener() {
                    if (it.isSuccessful) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }

        }
    }

}
