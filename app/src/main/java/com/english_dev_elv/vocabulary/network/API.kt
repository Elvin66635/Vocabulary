package com.english_dev_elv.vocabulary.network

import com.english_dev_elv.vocabulary.model.Topic
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val URL = "https://raw.githubusercontent.com/Elvin66635/Storage/main/"

interface API {

    @GET("topic.json")
    fun getTopics(): Call<List<Topic>>


    companion object {

        var retrofitService: API? = null

        fun getInstance(): API {



            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(API::class.java)
            }
            return retrofitService!!
        }
    }
}