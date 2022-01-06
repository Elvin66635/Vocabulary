package com.example.vocabulary.exampleRecView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabulary.R
import com.example.vocabulary.adapter.TopicAdapter
import com.example.vocabulary.databinding.ActivityExampleRecMainBinding
import com.example.vocabulary.network.API
import com.example.vocabulary.repository.TopicsRepository
import com.example.vocabulary.viewmodel.TopicViewModel
import com.example.vocabulary.viewmodel.TopicViewModelFactory

private const val TAG = "ExampleRecMain"

class ExampleRecMain : AppCompatActivity() {

    lateinit var binding: ActivityExampleRecMainBinding

   /* lateinit var viewModel: TopicViewModel
    val adapter = TopicAdapter()
    private val retrofitService = API.getInstance()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleRecMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar!!.hide()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
/*
        binding.recView.adapter = adapter
        binding.recView.layoutManager = LinearLayoutManager(this)


        viewModel =
            ViewModelProvider(this, TopicViewModelFactory(TopicsRepository(retrofitService)))
                .get(TopicViewModel::class.java)

        viewModel.topics.observe(this, Observer {
            adapter.setTopicList(it)
        })

        viewModel.errorMessage.observe(this, Observer {
            Log.d(TAG, "Error $it")
        })

        viewModel.getTopics()
    }*/
    }
}