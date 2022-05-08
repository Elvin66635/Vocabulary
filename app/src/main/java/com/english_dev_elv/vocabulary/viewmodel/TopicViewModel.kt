package com.english_dev_elv.vocabulary.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.english_dev_elv.vocabulary.model.Phonetic
import com.english_dev_elv.vocabulary.model.Topic
import com.english_dev_elv.vocabulary.repository.TopicsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "TopicViewModel"
class TopicViewModel(private val repository: TopicsRepository) : ViewModel() {

    val topics = MutableLiveData<List<Topic>>()
    val phonetics = MutableLiveData<List<Phonetic>>()
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

    fun getPhonetics() {
        val response = repository.getPhonetics()
        response.enqueue(object : Callback<List<Phonetic>>{
            override fun onResponse(call: Call<List<Phonetic>>, response: Response<List<Phonetic>>) {
                phonetics.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Phonetic>>, t: Throwable) {
                errorMessage.postValue(t.message)
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }
}