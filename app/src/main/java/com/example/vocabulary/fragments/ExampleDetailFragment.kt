package com.example.vocabulary.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vocabulary.R
import com.example.vocabulary.databinding.FragmentExampleDetailBinding
import com.example.vocabulary.databinding.FragmentMainQuizBinding
import com.example.vocabulary.utils.Constants

private const val TAG = "ExampleDetailFragment"
class ExampleDetailFragment : Fragment() {
    private var binding: FragmentExampleDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentExampleDetailBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalQuestions = arguments?.getInt("total_questions",0)
        val correctAnswer =  arguments?.getInt("correct_answers",0)

        val bundle = Bundle()
        if (correctAnswer != null) {
            bundle.putInt("correct_answers", correctAnswer)
        }

        binding?.resultTxt?.text = "Your score is $correctAnswer out of $totalQuestions"
        Log.d(TAG, "onViewCreated: $binding?.resultTxt?.text = \"Your score is $correctAnswer out of $totalQuestions\"")
        findNavController().navigate(R.id.action_exampleDetailFragment_to_mainQuizFragment, bundle)
    }
}