package com.sleepee.bondoman.presentation.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.commit
import com.sleepee.bondoman.R
import com.sleepee.bondoman.databinding.ActivityMainBinding
import com.sleepee.bondoman.databinding.ActivityRetakeTwibbonBinding
import com.sleepee.bondoman.presentation.fragment.TwibbonFragment

class RetakeTwibbonActivity: BaseActivity() {
    private lateinit var binding: ActivityRetakeTwibbonBinding
    private lateinit var image: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRetakeTwibbonBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val arrayOfBytes = intent.getByteArrayExtra("TwibbonPhoto")

        if (arrayOfBytes != null) {
            image = BitmapFactory.decodeByteArray(arrayOfBytes, 0, arrayOfBytes.size)
        }
        binding.twibbonResult.setImageBitmap(image)

        binding.retakeTwibbon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("twibbon-load", "twibbon-fragment")
            startActivity(intent)
        }
    }
}