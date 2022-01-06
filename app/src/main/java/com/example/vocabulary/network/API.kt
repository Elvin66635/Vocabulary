package com.example.vocabulary.network

import com.example.vocabulary.model.Topic
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface API {

    @GET("topic.json")
    fun getTopics(): Call<List<Topic>>


    companion object {

        var retrofitService: API? = null

        fun getInstance(): API {

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://raw.githubusercontent.com/Elvin66635/Storage/main/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(API::class.java)
            }
            return retrofitService!!
        }
    }
}