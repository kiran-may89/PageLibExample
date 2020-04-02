package com.example.pagelibexmp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.pagelibexmp.models.Article
import com.example.pagelibexmp.models.Feed
import com.example.pagelibexmp.services.Api
import com.example.pagelibexmp.services.ServicesConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class FeedDataSource(val query: String) : PageKeyedDataSource<Long, Article>() {
    private var api: Api
    private var networkState: MutableLiveData<String>
    private var initialLoading: MutableLiveData<String>;
    val networkLivedata: LiveData<String> get() = networkState
    val intialLiveData: LiveData<String> get() = initialLoading
    private val intialPage = 1L

    companion object {
        val LOADING: String = "LOADING";
        val RUNNING: String = "RUNNING";
        val SUCCESS: String = "SUCCESS";
        val FAILED: String = "FAILED";
    }


    init {
        api = ServicesConfig.getInstance();
        networkState = MutableLiveData()
        initialLoading = MutableLiveData()
    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Article>) {

        networkState.postValue(LOADING)
        initialLoading.postValue(LOADING)
        try {
            val response = api.getNewsFeed(query, intialPage, params.requestedLoadSize).execute()

            if (response.isSuccessful) {
                val articles = response.body()!!.articles
                callback.onResult(articles, null, intialPage+1);

                initialLoading.postValue(SUCCESS)
                networkState.postValue(SUCCESS)
            } else {
                initialLoading.postValue(FAILED)
                networkState.postValue(FAILED)
            }
        }catch (e:IOException){
            initialLoading.postValue(FAILED)
            networkState.postValue(FAILED)
        }

    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Article>) {

        networkState.postValue(LOADING)
        api.getNewsFeed(query, params.key, params.requestedLoadSize).enqueue(
            object : Callback<Feed> {
                override fun onFailure(call: Call<Feed>, t: Throwable) {
                    networkState.postValue(FAILED)
                }

                override fun onResponse(call: Call<Feed>, response: Response<Feed>) {
                    if (response.isSuccessful) {
                        val nextkey: Long = params.key+1
                        callback.onResult(response.body()?.articles ?: emptyList(), nextkey)
                        networkState.postValue(SUCCESS)
                    } else {
                        networkState.postValue(FAILED)
                    }

                }
            }
        )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Article>) {
        networkState.postValue(LOADING)
        initialLoading.postValue(LOADING)
        api.getNewsFeed(query, intialPage, params.requestedLoadSize)
            .enqueue(object : Callback<Feed> {
                override fun onFailure(call: Call<Feed>, t: Throwable) {

                    networkState.postValue(FAILED)
                }

                override fun onResponse(call: Call<Feed>, response: Response<Feed>) {
                    if (response.isSuccessful) {
                        val key = if (params.key > 1) params.key - 1 else 0
                        val articles = response.body()!!.articles
                        callback.onResult(articles, key);

                        initialLoading.postValue(SUCCESS)
                        networkState.postValue(SUCCESS)
                    } else {
                        initialLoading.postValue(FAILED)
                        networkState.postValue(FAILED)
                    }


                }
            })
    }
}