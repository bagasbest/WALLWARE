package com.application.wallware.catalogue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.application.wallware.databinding.ActivityCatalogueBinding
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager

class CatalogueActivity : AppCompatActivity() {

    private var binding: ActivityCatalogueBinding? = null
    private var adapter: CatalogueAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogueBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initRecylerView()
        initViewModel("all")

        binding?.searchEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(searchQuery: Editable?) {
                val query = searchQuery.toString()
                if(query.isEmpty()) {
                    initRecylerView()
                    initViewModel("all")
                } else {
                    initRecylerView()
                    initViewModel(query)
                }
            }
        })


        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecylerView() {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(this)
        adapter = CatalogueAdapter()
        binding?.recyclerView?.adapter = adapter
    }


    private fun initViewModel(query: String) {
        val viewModel = ViewModelProvider(this)[CatalogueViewModel::class.java]

        binding?.progressBar?.visibility = View.VISIBLE

        if(query == "all") {
            viewModel.setCatalogue()
        } else {
            viewModel.setCatalogueByQuery(query)
        }
        viewModel.getCatalogue().observe(this) { catalogue ->
            if (catalogue.size > 0) {
                binding!!.noData.visibility = View.GONE
                adapter!!.setData(catalogue)
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