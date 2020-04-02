package com.example.pagelibexmp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pagelibexmp.adapter.PagedAdapter
import com.example.pagelibexmp.databinding.ActivityMainBinding
import com.example.pagelibexmp.models.Article
import com.example.pagelibexmp.services.ServicesConfig
import com.example.pagelibexmp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel:MainViewModel
    lateinit var adapter: PagedAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val binding:ActivityMainBinding = DataBindingUtil. setContentView(this,R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        adapter = PagedAdapter()

        viewModel.networkState.observe(this, Observer < String>{
            adapter.setState(it)
        })
        viewModel.pagedLiveData.observe(this,
            Observer<PagedList<Article>> {
                adapter.submitList(it)
            })
binding.listFeed.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.listFeed.adapter = adapter

    }
}
