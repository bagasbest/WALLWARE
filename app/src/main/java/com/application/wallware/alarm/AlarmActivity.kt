package com.application.wallware.alarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.wallware.catalogue.CatalogueActivity
import com.application.wallware.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {

    private var binding: ActivityAlarmBinding? = null
    private var adapter: AlarmAdapter? = null

    override fun onResume() {
        super.onResume()
        initRecyclerView()
        initViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.imageButton2?.setOnClickListener {
            startActivity(Intent(this, CatalogueActivity::class.java))
        }

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

    }

    private fun initRecyclerView() {
        binding?.rvAlarm?.layoutManager = LinearLayoutManager(this)
        adapter = AlarmAdapter()
        binding?.rvAlarm?.adapter = adapter
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListAlarm()
        viewModel.getAlarmList().observe(this) { alarm ->
            if (alarm.size > 0) {
                binding!!.noData.visibility = View.GONE
                adapter!!.setData(alarm)
            } else {
                binding!!.noData.visibility = View.VISIBLE
            }
            binding!!.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}