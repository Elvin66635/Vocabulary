package com.example.vocabulary

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.vocabulary.databinding.ActivityMainBinding
import com.example.vocabulary.databinding.ActivityQuizQuestionsBinding
import com.example.vocabulary.model.Question
import com.example.vocabulary.utils.Constants
import com.example.vocabulary.utils.Constants.CORRECT_ANSWERS
import com.example.vocabulary.utils.Constants.TOTAL_QUESTIONS

private const val TAG = "QuizQuestionsActivity"

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityQuizQuestionsBinding

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mQuestionsList = Constants.getQuestions()

        setQuestion()
        binding.optionOneTxt.setOnClickListener(this)
        binding.optionTwoTxt.setOnClickListener(this)
        binding.optionThreeTxt.setOnClickListener(this)
        binding.optionFourTxt.setOnClickListener(this)
        binding.submitBtn.setOnClickListener(this)

    }

    private fun setQuestion() {

        val question = mQuestionsList!![mCurrentPosition - 1]
        defaultOptionsView()
        if (mCurrentPosition == mQuestionsList!!.size) {
            binding.submitBtn.text = "Finish"
        } else {
            binding.submitBtn.text = "Submit"
        }


        binding.progressBar.progress = mCurrentPosition
        binding.tvProgress.text = "$mCurrentPosition/" + binding.progressBar.max
        binding.tvQuestion.text = question.question
        binding.imageQuestion.setImageResource(question.image)
        binding.optionOneTxt.text = question.optionOne
        binding.optionTwoTxt.text = question.optionTwo
        binding.optionThreeTxt.text = question.optionThree
        binding.optionFourTxt.text = question.optionFour
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, binding.optionOneTxt)
        options.add(1, binding.optionTwoTxt)
        options.add(2, binding.optionThreeTxt)
        options.add(3, binding.optionFourTxt)

        for (option in options) {
            option.setTextColor(Color.GRAY)
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.optionOneTxt -> {
                selectedOptionView(binding.optionOneTxt, 1)
            }
            R.id.optionTwoTxt -> {
                selectedOptionView(binding.optionTwoTxt, 2)
            }
            R.id.optionThreeTxt -> {
                selectedOptionView(binding.optionThreeTxt, 3)
            }
            R.id.optionFourTxt -> {
                selectedOptionView(binding.optionFourTxt, 4)
            }
            R.id.submitBtn -> {
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++
                    when {
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                           val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(TOTAL_QUESTIONS, mQuestionsList!!.size)
                         //   startActivity(intent)
                        }
                    }
                } else {
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border)
                    }else{
                        mCorrectAnswers++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border)
                    if (mCurrentPosition == mQuestionsList!!.size) {
                        binding.submitBtn.text = "Finish"
                    } else {
                        binding.submitBtn.text = "Go to next Question"
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                binding.optionOneTxt.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                binding.optionTwoTxt.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                binding.optionThreeTxt.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                binding.optionFourTxt.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int) {
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNumber


        tv.setTextColor(Color.BLACK)
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border)
    }
}