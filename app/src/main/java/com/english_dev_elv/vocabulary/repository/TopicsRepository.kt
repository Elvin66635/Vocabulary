package com.english_dev_elv.vocabulary.repository

import com.english_dev_elv.vocabulary.network.API

class TopicsRepository(private val topicsApi: API) {
    fun getTopics() = topicsApi.getTopics()
}