package com.example.clothingecsite_30.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.clothingecsite_30.R
import com.google.firebase.auth.ktx.auth
import com.example.clothingecsite_30.databinding.ActivitySplashBinding
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    private val fireAuth = Firebase.auth
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

//        val typeface: Typeface =
//            Typeface.createFromAsset(assets, "carbon bl.ttf")
//        binding.tvAppName.typeface = typeface

        Handler().postDelayed({
            val currentUserID = fireAuth.uid
            if (currentUserID != null) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            }
            finish()
        }, 2500)
    }
}