package com.example.dopaminemenu.vibemenu.splash

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.dopaminemenu.MainActivity
import com.example.dopaminemenu.R
import com.example.dopaminemenu.vibemenu.signin.SignupActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)


        val text = findViewById<TextView>(R.id.welcome)
        text.setText("Welcome")

        val desc = findViewById<TextView>(R.id.desc)
        desc.setText(" Let's Craft Your Perfect Day By Discovering Your Personalized Dopamine Menu.")

        val btn = findViewById<Button>(R.id.button)
        btn.setText("Get Started")
        btn.setOnClickListener {

            btn.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(100)
                .withEndAction {
                    btn.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .duration = 100
                }

            val intent = Intent(this@SplashActivity, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }


        val animation = findViewById<LottieAnimationView>(R.id.anime)
        animation.repeatCount = ValueAnimator.INFINITE
//        animation.speed = 3f
        animation.playAnimation()
    }
}

//        animation.addAnimatorListener(object : Animator.AnimatorListener {
//
//            override fun onAnimationStart(animation: Animator) {}
//
//            override fun onAnimationEnd(animation: Animator) {
//                val intent = Intent(this@SplashActivity, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//            override fun onAnimationCancel(animation: Animator) {}
//
//            override fun onAnimationRepeat(animation: Animator) {}
//        }
//        )
//    }
//}


//        for frames

//        val animeImage = findViewById<ImageView>(R.id.anime)
//        val anime = animeImage.drawable as AnimationDrawable
//        anime.start()
