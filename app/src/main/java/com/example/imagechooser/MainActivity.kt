package com.example.imagechooser

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URI

class MainActivity : AppCompatActivity() {
    private val imageChooseResultCode = 343

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { openImageChooser() }
    }

    private fun openImageChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }

        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        startActivityForResult(Intent.createChooser(intent, title), imageChooseResultCode)
    }

    private fun handleImagesFromGallery(data: Intent?) {
        val images = mutableListOf<Uri>()
        val uri = data?.data
        if (uri.isNull() && data?.clipData.isNull().not()) {
            data?.clipData?.let { clipData ->
                val count: Int = clipData.itemCount

                for (clip in 0 until count) {
                    clipData.getItemAt(clip)?.uri?.let { uri ->
                        images.add(uri)
                    }
                }
                countFiles.text = images.size.toString()
            }
        } else {
            Toast.makeText(this, "has been choosed 1 file", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return super.onActivityResult(requestCode, resultCode, data)
        }

        when (requestCode) {
            imageChooseResultCode -> handleImagesFromGallery(data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    inline fun Any?.isNull(): Boolean {
        return when (this) {
            is List<*> -> {
                this.isNotEmpty()
            }
            !is List<*> -> {
                this == null
            }
            else -> false
        }
    }
}