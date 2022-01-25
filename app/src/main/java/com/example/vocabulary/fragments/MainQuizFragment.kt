package com.example.vocabulary.fragments

import android.app.AlertDialog
import android.content.Context
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
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.mig35.carousellayoutmanager.CarouselLayoutManager
import com.mig35.carousellayoutmanager.CenterScrollListener





private const val TAG = "MainQuizFragment"

class MainQuizFragment : Fragment() {
    private var binding: FragmentMainQuizBinding? = null
    lateinit var viewModel: TopicViewModel
    private lateinit var adapter: TopicAdapter
    private val retrofitService = API.getInstance()
    private var currentProgress = 0
    var saveResult = 0

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

        if (saveResult != null) {
            val sharedPreferences: SharedPreferences =
                requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            saveResult = sharedPreferences.getInt("result", 0)
            Log.d(TAG, "$saveResult")
        } else {
            Log.d(TAG, "null ")
        }

        binding?.mainProgressbar?.max = 100

        binding?.mainProgressbar?.progress = saveResult

        binding?.recyclerViewTitles?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = TopicAdapter(TopicAdapter.OnClickListener { topic ->

            val dataBundle = Bundle()
            dataBundle.putString("image", topic.image)
            dataBundle.putString("title", topic.title)
            dataBundle.putString("question", topic.question)
            dataBundle.putInt("saved_result", saveResult)
            dataBundle.putParcelableArrayList("list", topic.quizDetails)


            val dialog = MaterialDialog(requireContext())
                .noAutoDismiss()
                .customView(R.layout.layout_checking_buttons)

            dialog.findViewById<Button>(R.id.testBtn).setOnClickListener {
                val dataBundle = Bundle()
                dataBundle.putString("image", topic.image)
                dataBundle.putString("title", topic.title)
                dataBundle.putString("question", topic.question)
                dataBundle.putInt("saved_result", saveResult)
                dataBundle.putParcelableArrayList("list", topic.quizDetails)
                findNavController().navigate(
                    R.id.action_mainQuizFragment_to_quizDetailFragment,
                    dataBundle
                )
                dialog.dismiss()
            }

            dialog.findViewById<Button>(R.id.theoryBtn).setOnClickListener {
                findNavController().navigate(
                R.id.action_mainQuizFragment_to_theoryFragment,
                dataBundle
            )
                dialog.dismiss()
            }
            dialog.show()

        })

        loadData()
    }

    private fun showDialog() {

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