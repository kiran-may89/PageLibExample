package com.example.pagelibexmp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.pagelibexmp.data.FeedDataFactory
import com.example.pagelibexmp.data.FeedDataSource
import com.example.pagelibexmp.models.Article
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainViewModel :ViewModel() {
      var networkState:LiveData<String>
      var pagedLiveData:LiveData<PagedList<Article>>

    init {

        val feedDataFactory:FeedDataFactory = FeedDataFactory("Android")
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(2)
            .setPageSize(2)
            .build()

        pagedLiveData = LivePagedListBuilder(feedDataFactory,config).setFetchExecutor( Executors.newFixedThreadPool(5)).build()
        networkState = Transformations.switchMap(feedDataFactory.sourceLiveData) { input: FeedDataSource? -> input!!.networkLivedata }

    }


}