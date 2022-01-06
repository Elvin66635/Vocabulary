package com.example.vocabulary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vocabulary.repository.TopicsRepository
import java.lang.IllegalArgumentException

class TopicViewModelFactory constructor(private val repository: TopicsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TopicViewModel::class.java)) {
            TopicViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}