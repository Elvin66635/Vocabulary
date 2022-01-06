package com.example.vocabulary.repository

import com.example.vocabulary.network.API

class TopicsRepository(private val topicsApi: API) {
    fun getTopics() = topicsApi.getTopics()
}