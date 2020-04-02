package com.example.pagelibexmp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.pagelibexmp.models.Article
import com.example.pagelibexmp.models.Feed

class FeedDataFactory(val query:String):DataSource.Factory<Long,Article>(){
    private  val liveData:MutableLiveData<FeedDataSource> = MutableLiveData()
    private  lateinit var feedDataSource: FeedDataSource

    override fun create(): DataSource<Long, Article> {
        feedDataSource = FeedDataSource(query);
        liveData.postValue(feedDataSource)
        return  feedDataSource;

    }
    public val sourceLiveData:LiveData<FeedDataSource> get() = liveData
}