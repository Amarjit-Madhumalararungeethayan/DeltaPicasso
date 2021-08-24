package com.example.picasso

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.picasso.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {

    lateinit var binding : ActivityHomeBinding

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#FFFFFFFF")
        }

        binding.gal.setOnClickListener(){
            val intent = Intent(this, Editor::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.right, R.anim.nothing)
            finish()
        }
    }
}