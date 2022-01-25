package com.example.vocabulary.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.vocabulary.R
import com.example.vocabulary.databinding.FragmentQuizDetailBinding
import com.example.vocabulary.model.Question
import com.example.vocabulary.model.QuizDetails
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

private const val TAG = "QuizDetailFragment"

class QuizDetailFragment : Fragment(), View.OnClickListener, TextToSpeech.OnInitListener, Animation.AnimationListener {
    private lateinit var binding: FragmentQuizDetailBinding
    private var tts: TextToSpeech? = null
    private lateinit var bundle: Bundle

    private var mCurrentPosition: Int = 1
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
    private var savedResult = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        savedResult = arguments?.getInt("saved_result")!!
        mQuestionsList = arguments?.getParcelableArrayList("list")

        bundle = Bundle()

        setQuestion()
        binding.optionOneTxt.setOnClickListener(this)
        binding.optionTwoTxt.setOnClickListener(this)
        binding.optionThreeTxt.setOnClickListener(this)
        binding.submitBtn.setOnClickListener(this)
        binding.speachText.setOnClickListener(this)

        tts = TextToSpeech(requireContext(), this)
    }

    private fun defaultOptionsView() {
        checkedPressOptions()
        val options = ArrayList<TextView>()
        options.add(0, binding.optionOneTxt)
        options.add(1, binding.optionTwoTxt)
        options.add(2, binding.optionThreeTxt)

        for (option in options) {
            option.setTextColor(Color.parseColor("#1F1F1F"))
            option.typeface = Typeface.DEFAULT
            option.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.default_option_border)

        }
    }

    @SuppressLint("ResourceAsColor")
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

            R.id.speachText -> {
                speakOut()
            }

            R.id.submitBtn -> {
                isClickableOtherOptions(true)
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++
                    when {
                        mCurrentPosition <= mQuestionsList?.size!! -> {
                            setQuestion()
                            noVisibleView()
                        }
                        else -> {
                            bundle.putInt("correct_answers", mCorrectAnswers)
                            bundle.putInt("total_questions", mQuestionsList!!.size)
                            bundle.putString("image", image)
                            bundle.putString("title", title)
                            bundle.putInt("saved_result", savedResult)
                            findNavController().navigate(
                                R.id.action_quizDetailFragment_to_exampleDetailFragment,
                                bundle
                            )
                        }
                    }
                } else {
                    isClickableOtherOptions(false)
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        binding.falseView.visibility = View.VISIBLE
                        val animation = AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.nav_default_exit_anim
                        )
                        binding.falseView.startAnimation(animation)
                        val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.fail)
                        mediaPlayer.start()
                        vibrateButton()
                        binding.wrongText.text = "Правильно: ${question.correctWord}"

                    } else {
                        binding.trueView.visibility = View.VISIBLE
                        val animation = AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.nav_default_enter_anim
                        )
                        binding.trueView.startAnimation(animation)
                        //   binding.view.setBackgroundColor(Color.parseColor("#32CD32"))
                        vibrateButton()
                        binding.trueText.text = "Верно"
                        val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.success)
                        mediaPlayer.start()
                        mCorrectAnswers++
                    }

                    if (mCurrentPosition == mQuestionsList?.size) {
                        binding.submitBtn.text = "Завершить"
                      //  visibleView()

                    } else {
                        binding.submitBtn.text = "Дальше"
                    }
                    mSelectedOptionPosition = 0
                }
            }

        }

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
        // noVisibleView()
        val items = mQuestionsList
        val question = items!![mCurrentPosition - 1]

        defaultOptionsView()
        if (mCurrentPosition == items.size) {
            binding.submitBtn.text = "Завершить"

        } else {
            binding.submitBtn.text = "Подтвердить"
        }
        binding.progressBar.progress = mCurrentPosition
        binding.progressBar.max = mQuestionsList!!.size
    //    binding.tvProgress.text = "$mCurrentPosition/" + binding.progressBar.max

        for (i in 0 until items.count()) {
            Glide.with(requireContext()).load(question.imageDetail.toString())
                .transform(RoundedCorners(35))
                .into(binding.imageQuestion)

            binding.tvQuestion.text = question.question
            binding.optionOneTxt.text = question.optionOne
            binding.optionTwoTxt.text = question.optionTwo
            binding.optionThreeTxt.text = question.optionThree
        }
    }

    private fun checkedPressOptions() {
        if (binding.optionOneTxt.isPressed ||
            binding.optionTwoTxt.isPressed ||
            binding.optionThreeTxt.isPressed
        ) {
            binding.submitBtn.visibility = View.VISIBLE
        } else {
            binding.submitBtn.visibility = View.INVISIBLE
        }
    }

    private fun isClickableOtherOptions(clickable: Boolean): Boolean {
        binding.optionOneTxt.isClickable = clickable
        binding.optionTwoTxt.isClickable = clickable
        binding.optionThreeTxt.isClickable = clickable
        return clickable
    }

    private fun noVisibleView() {
        binding.trueView.visibility = View.GONE
        binding.falseView.visibility = View.GONE
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.UK)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                Log.e("TTS","Ok")
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }
    private fun speakOut() {
        val items = mQuestionsList
        val question = items!![mCurrentPosition - 1]
        val text = question.question.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    fun vibrateButton(){
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(30)
        }
    }

    override fun onAnimationStart(p0: Animation?) {
    }

    override fun onAnimationEnd(p0: Animation?) {
    }

    override fun onAnimationRepeat(p0: Animation?) {
    }
}