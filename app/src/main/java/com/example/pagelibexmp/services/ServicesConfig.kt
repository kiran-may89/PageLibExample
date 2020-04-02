package com.example.pagelibexmp.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServicesConfig private constructor(){

    object  SingleTon{
        val  api:Api = create();
        
    }
    companion object{
        private var api:Api?=null;
        private const val BASE_URL = "https://newsapi.org"
        public fun getInstance():Api=
            api?: synchronized(this){
                api?: create().also {
                    api = it;
                }
            }


        private fun create():Api{
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder();
            client.addInterceptor(logging)
            val builder:Retrofit.Builder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client.build()).baseUrl(BASE_URL)

            return  builder.build().create(Api::class.java)

        }
    }


}