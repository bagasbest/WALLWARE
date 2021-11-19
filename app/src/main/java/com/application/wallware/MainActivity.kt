package com.application.wallware

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.wallware.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import com.google.android.gms.tasks.OnSuccessListener

import com.google.firebase.storage.FirebaseStorage







class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Glide.with(this)
            .load(R.drawable.logo)
            .into(binding!!.imageView)

        binding?.next?.setOnClickListener {
            startActivity(Intent(this, HomeActivity2::class.java))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}