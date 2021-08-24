package com.example.picasso

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.view.isVisible
import com.example.picasso.databinding.ActivityEditorBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class Editor : AppCompatActivity() {

    lateinit var binding : ActivityEditorBinding
    lateinit var bitmapTemp : Bitmap
    lateinit var bitmapFinal : Bitmap

    override fun onBackPressed() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.right, R.anim.nothing)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#FFFFFFFF")
        }

        binding.pic.setOnClickListener() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        binding.gray.setOnClickListener(){
            var h = bitmapTemp.height
            var w = bitmapTemp.width

            var bmpGrayscale = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            var c = Canvas(bmpGrayscale)

            var paint = Paint()
            var cm = ColorMatrix()
            cm.setSaturation(0F)

            var f = ColorMatrixColorFilter(cm)
            paint.setColorFilter(f)
            c.drawBitmap(bitmapTemp,0F,0F,paint)

            bitmapFinal = bmpGrayscale
            binding.pic.setImageBitmap(bmpGrayscale)
        }

        binding.save.setOnClickListener(){

            val root = Environment.getExternalStorageDirectory().toString()
            val myDir = File("$root/req_images")
            myDir.mkdirs()
            val generator = Random()
            var n = 10000
            n = generator.nextInt(n)
            val fname = "Image-$n.jpg"
            val file = File(myDir, fname)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                bitmapFinal.compress(Bitmap.CompressFormat.JPEG, 90, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        binding.share.setOnClickListener(){
            val intent = Intent(Intent.ACTION_SEND)

            intent.putExtra(
                Intent.EXTRA_STREAM, getTheImageUri(this, bitmapFinal!!))
            intent.type = "img/*"


            intent.putExtra(
                Intent.EXTRA_TEXT, ("Picasso"))
            intent.type = "text/plain"

            this.startActivity(Intent.createChooser(intent, "Send To"))
        }

    }

    private fun getTheImageUri(inContext: Context, inImage : Bitmap): Uri? {

        val noBytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, noBytes)

        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver,inImage, "TL", null)
        return Uri.parse(path)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            val selectedUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedUri)
            bitmapTemp = bitmap

            binding.pic.setImageBitmap(bitmap)

            binding.tag.isVisible = false
        }
    }
}