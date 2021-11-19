package com.application.wallware

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.wallware.databinding.ActivityFaqactivityBinding

class FAQActivity : AppCompatActivity() {

    private var binding: ActivityFaqactivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqactivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}