package com.example.picasso

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.picasso.databinding.ActivityResultBinding
import java.io.File
import java.io.FileOutputStream


class Result : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val bitty = intent.getParcelableExtra<Parcelable>("BitmapImage") as Bitmap?

        binding.disp.setImageBitmap(bitty)

        binding.gray.setOnClickListener(){

            val root = Environment.getExternalStorageDirectory().toString()
            val myDir = File(root)
            myDir.mkdirs()
            val fname = "Image-" + "${(0..2024324879823).random()}" + ".jpg"
            val file = File(myDir, fname)
            if (file.exists()) file.delete()
            Log.i("LOAD", root + fname)
            try {
                val out = FileOutputStream(file)
                bitty?.compress(Bitmap.CompressFormat.JPEG, 90, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            MediaScannerConnection.scanFile(
                this, arrayOf(file.toString()), null
            ) { path, uri ->
                Log.i("ExternalStorage", "Scanned $path:")
                Log.i("ExternalStorage", "-> uri=$uri")
            }
        }

        binding.text.setOnClickListener(){

        }

        binding.save.setOnClickListener(){
            var intent = Intent(this, Home::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.right, R.anim.nothing)
            finish()
        }

    }
}