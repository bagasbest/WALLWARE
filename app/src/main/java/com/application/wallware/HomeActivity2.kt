package com.application.wallware

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.wallware.alarm.AlarmActivity
import com.application.wallware.catalogue.CatalogueActivity
import com.application.wallware.databinding.ActivityHome2Binding
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

class HomeActivity2 : AppCompatActivity() {

    private var binding: ActivityHome2Binding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding?.root)

        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.header, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.wall_garden1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.wall_garden2, ScaleTypes.CENTER_CROP))

        binding?.imageSlider?.setImageList(imageList, ScaleTypes.CENTER_CROP)


        binding?.cardView?.setOnClickListener {
            startActivity(Intent(this, AlarmActivity::class.java))
        }

        binding?.cardView2?.setOnClickListener {
            startActivity(Intent(this, CatalogueActivity::class.java))
        }

        binding?.cardView3?.setOnClickListener {
            startActivity(Intent(this, FAQActivity::class.java))
        }

        binding?.web?.setOnClickListener {
            val url = "https://wallware.id/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }


        binding?.ig?.setOnClickListener {
            val url = "https://instagram.com/wallwareid?utm_medium=copy_link"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}