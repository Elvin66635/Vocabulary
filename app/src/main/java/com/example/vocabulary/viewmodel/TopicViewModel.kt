package com.example.vocabulary.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vocabulary.model.QuizDetails
import com.example.vocabulary.model.Topic
import com.example.vocabulary.repository.TopicsRepository
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "TopicViewModel"
class TopicViewModel(private val repository: TopicsRepository) : ViewModel() {

    val topics = MutableLiveData<List<Topic>>()
    val errorMessage = MutableLiveData<String>()







        fun getTopics() {
            val response = repository.getTopics()
            response.enqueue(object : Callback<List<Topic>>{
                override fun onResponse(call: Call<List<Topic>>, response: Response<List<Topic>>) {
                    topics.postValue(response.body()?.sortedByDescending { list -> list.id })
                    val items = response.body()
                    if (items != null){
                        for (i in 0 until items.count()){
                            val details = items[i].quizDetails
                            Log.d(TAG, "onResponse: $details")
                        }
                    }else
                        Log.e("RETROFIT_ERROR", response.code().toString())
                }

                override fun onFailure(call: Call<List<Topic>>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    Log.d(TAG, "onFailure: ${t.message}")
                }

        })
    }
}