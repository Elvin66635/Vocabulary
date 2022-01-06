package com.example.vocabulary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vocabulary.databinding.ActivityQuizQuestionsBinding
import com.example.vocabulary.databinding.ActivityResultBinding
import com.example.vocabulary.utils.Constants

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS,0)
        val correctAnswer = intent.getIntExtra(Constants.CORRECT_ANSWERS,0)

        binding.resultQuiz.text = "Your score is $correctAnswer out of $totalQuestions"

    }
}