package com.example.pagelibexmp.services

import com.example.pagelibexmp.models.Feed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    companion object{
        const val APIKEY:String = "079dac74a5f94ebdb990ecf61c8854b7"
    }



    @GET("/v2/everything")
    fun getNewsFeed(
        @Query("q") q: String,

        @Query("page") page: Long,
        @Query("pageSize") pageSize: Int,
        @Query("apiKey") key:String = APIKEY
    ): Call<Feed>
}