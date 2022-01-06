package com.example.vocabulary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vocabulary.R
import com.example.vocabulary.databinding.FragmentExampleDetailBinding
import com.example.vocabulary.databinding.FragmentMainQuizBinding
import com.example.vocabulary.utils.Constants

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
        binding?.resultTxt?.text = "Your score is $correctAnswer out of $totalQuestions"
    }
}