package com.example.vocabulary.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.opengl.Visibility
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.vocabulary.R
import com.example.vocabulary.ResultActivity
import com.example.vocabulary.databinding.FragmentQuizDetailBinding
import com.example.vocabulary.model.Question
import com.example.vocabulary.model.QuizDetails
import com.example.vocabulary.model.Topic
import com.example.vocabulary.utils.Constants
import kotlin.properties.Delegates

private const val TAG = "QuizDetailFragment"

class QuizDetailFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentQuizDetailBinding


    private var mCurrentPosition: Int = 1

    //   private var mQuestionsList: ArrayList<Topic>? = null
    // private var mQuestionsList: ArrayList<Topic>? = null
    private var mQuestionsList: ArrayList<QuizDetails>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private lateinit var optionOne: String
    private lateinit var optionTwo: String
    private lateinit var optionThree: String
    private lateinit var optionFour: String
    private lateinit var title: String
    private lateinit var question: String
    private lateinit var image: String
    private var correctAnswer = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuizDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = arguments?.getString("title").toString()
        question = arguments?.getString("question").toString()
        optionOne = arguments?.getString("optionOne").toString()
        optionTwo = arguments?.getString("optionTwo").toString()
        optionThree = arguments?.getString("optionThree").toString()
        optionFour = arguments?.getString("optionFour").toString()
        image = arguments?.getString("image").toString()
        correctAnswer = arguments?.getInt("correctAnswer")!!
        mQuestionsList = arguments?.getParcelableArrayList("list")

        Log.d(TAG, "onViewCreated: $mQuestionsList")

        setQuestion()
        binding.optionOneTxt.setOnClickListener(this)
        binding.optionTwoTxt.setOnClickListener(this)
        binding.optionThreeTxt.setOnClickListener(this)
        binding.submitBtn.setOnClickListener(this)
    }

    private fun defaultOptionsView() {
        checkedPressOptions()
        val options = ArrayList<TextView>()
        options.add(0, binding.optionOneTxt)
        options.add(1, binding.optionTwoTxt)
        options.add(2, binding.optionThreeTxt)

        for (option in options) {
            option.setTextColor(Color.GRAY)
            option.typeface = Typeface.DEFAULT
            option.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.default_option_border)

        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onClick(v: View?) {
      //  noVisibleView()
       // checkedPressOptions()
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


            R.id.submitBtn -> {
                if (mSelectedOptionPosition == 0)  {

                    mCurrentPosition++
                    when {
                        mCurrentPosition <= mQuestionsList?.size!! -> {

                            setQuestion()
                        }
                        else -> {
                            val bundle = Bundle()
                            bundle.putInt("correct_answers", mCorrectAnswers)
                            bundle.putInt("total_questions", mQuestionsList!!.size)
                            findNavController().navigate(
                                R.id.action_quizDetailFragment_to_exampleDetailFragment,
                                bundle
                            )

                        }
                    }
                } else {
                    noVisibleView()
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        visibleView()
                  //      answerView(mSelectedOptionPosition, R.drawable.wrong_option_border)

                    } else {
                        mCorrectAnswers++
                    }
                  //  answerView(question.correctAnswer, R.drawable.correct_option_border)
                  //  visibleView()
                    if (mCurrentPosition == mQuestionsList?.size) {
                        binding.submitBtn.text = "Завершить"

                    } else {
                        binding.view.setBackgroundColor(Color.GREEN)
                        binding.submitBtn.text = "Дальше"
                    }
                    noVisibleView()
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {

        when (answer) {
            1 -> {
                binding.optionOneTxt.background =
                    ContextCompat.getDrawable(requireContext(), drawableView)
            }
            2 -> {
                binding.optionTwoTxt.background =
                    ContextCompat.getDrawable(requireContext(), drawableView)
            }
            3 -> {
                binding.optionThreeTxt.background =
                    ContextCompat.getDrawable(requireContext(), drawableView)
            }
        }
      //  visibleView()
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int) {
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNumber

        tv.setTextColor(Color.BLACK)
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.selected_option_border)
    }

    private fun setQuestion() {
        val items = mQuestionsList
        val question = items!![mCurrentPosition - 1]

        defaultOptionsView()
        if (mCurrentPosition == items!!.size) {
            Log.d(TAG, "setQuestion: ${items.size}")
            binding.submitBtn.text = "Завершить"
        } else {
            binding.submitBtn.text = "Подтвердить"
        }

        binding.progressBar.progress = mCurrentPosition
        binding.tvProgress.text = "$mCurrentPosition/" + binding.progressBar.max

        if (items != null) {
            for (i in 0 until items.count()) {
                val details = items[i]
                binding.tvQuestion.text = question.question
                binding.optionOneTxt.text = question.optionOne
                binding.optionTwoTxt.text = question.optionTwo
                binding.optionThreeTxt.text = question.optionThree
                Log.d(TAG, "FOR: $details")
            }
        }
    }
    private fun checkedPressOptions(){
        if (binding.optionOneTxt.isPressed ||
            binding.optionTwoTxt.isPressed ||
            binding.optionThreeTxt.isPressed){
            binding.submitBtn.visibility = View.VISIBLE
        }else{
            binding.submitBtn.visibility = View.INVISIBLE
        }
    }
    private fun isVisibleOptions(){

        binding.optionOneTxt.visibility = View.INVISIBLE
        binding.optionTwoTxt.visibility = View.INVISIBLE
        binding.optionThreeTxt.visibility = View.INVISIBLE
    }
    private fun visibleView(){
        binding.view.visibility = View.VISIBLE
        if (mSelectedOptionPosition == mCorrectAnswers){
            binding.view.setBackgroundColor(Color.GREEN)
            binding.trueOrWrongText.text = "Верно"
        }else{
            binding.view.setBackgroundColor(Color.RED)
            binding.trueOrWrongText.text = "Неверно"
        }
    }
    private fun noVisibleView(){
        binding.view.visibility = View.GONE
    }
}