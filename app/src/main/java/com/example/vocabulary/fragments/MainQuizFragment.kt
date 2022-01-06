package com.example.vocabulary.fragments

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabulary.R
import com.example.vocabulary.adapter.TopicAdapter
import com.example.vocabulary.databinding.ActivityExampleRecMainBinding
import com.example.vocabulary.databinding.FragmentMainQuizBinding
import com.example.vocabulary.model.QuizDetails
import com.example.vocabulary.model.Topic
import com.example.vocabulary.network.API
import com.example.vocabulary.repository.TopicsRepository
import com.example.vocabulary.viewmodel.TopicViewModel
import com.example.vocabulary.viewmodel.TopicViewModelFactory

private const val TAG = "MainQuizFragment"

class MainQuizFragment : Fragment() {
    private var binding: FragmentMainQuizBinding? = null
    lateinit var viewModel: TopicViewModel
    private lateinit var adapter: TopicAdapter
    private val retrofitService = API.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentMainQuizBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recyclerViewTitles?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = TopicAdapter(TopicAdapter.OnClickListener { topic ->
            val dataBundle = Bundle()
            dataBundle.putString("image", topic.image)
            dataBundle.putString("title", topic.title)
            dataBundle.putString("question", topic.question)
            dataBundle.putParcelableArrayList("list", topic.quizDetails)

            findNavController().navigate(
                R.id.action_mainQuizFragment_to_quizDetailFragment,
                dataBundle
            )
            Toast.makeText(requireContext(), topic.title, Toast.LENGTH_SHORT).show()
        })

        loadData()
    }

    private fun loadData() {
        viewModel =
            ViewModelProvider(this, TopicViewModelFactory(TopicsRepository(retrofitService)))
                .get(TopicViewModel::class.java)

        viewModel.topics.observe(requireActivity(), Observer {
            adapter.submitList(it)
            binding?.recyclerViewTitles?.adapter = adapter
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            Log.d(TAG, "Error $it")
        })

        viewModel.getTopics()
    }
}