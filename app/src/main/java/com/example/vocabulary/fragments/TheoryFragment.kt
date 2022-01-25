package com.example.vocabulary.fragments

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.vocabulary.R
import com.example.vocabulary.adapter.TheoryAdapter
import com.example.vocabulary.adapter.TopicAdapter
import com.example.vocabulary.databinding.FragmentQuizDetailBinding
import com.example.vocabulary.databinding.FragmentTheoryBinding
import com.example.vocabulary.model.QuizDetails
import com.example.vocabulary.network.API
import com.example.vocabulary.repository.TopicsRepository
import com.example.vocabulary.viewmodel.TopicViewModel
import com.example.vocabulary.viewmodel.TopicViewModelFactory
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview
import com.mig35.carousellayoutmanager.CarouselLayoutManager
import com.mig35.carousellayoutmanager.CenterScrollListener
import com.mig35.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.mig35.carousellayoutmanager.DefaultChildSelectionListener
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "TheoryFragment"

class TheoryFragment : Fragment(), View.OnClickListener, TextToSpeech.OnInitListener {

    private lateinit var binding: FragmentTheoryBinding
    private var mQuestionsList: ArrayList<QuizDetails>? = null
    private var tts: TextToSpeech? = null
    private var mCurrentPosition: Int = 1
    private lateinit var titleTheory: String
    lateinit var viewModel: TopicViewModel
    private val retrofitService = API.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTheoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mQuestionsList = arguments?.getParcelableArrayList("list")

        tts = TextToSpeech(requireContext(), this)
        binding.submitTheoryBtn.setOnClickListener(this)
        binding.theorySpeach.setOnClickListener(this)
        setQuestion()

/*
        binding.listHorizontal.addOnScrollListener(CenterScrollListener())
        binding.listHorizontal.set3DItem(true)
        binding.listHorizontal.setInfinite(true)
        binding.listHorizontal.setAlpha(true)
        binding.listHorizontal.setFlat(true)
        val carouselLayoutManager =  binding.listHorizontal.getCarouselLayoutManager()
        val currentlyCenterPosition =  binding.listHorizontal.getSelectedPosition()*/

        /*     adapter = TheoryAdapter(TheoryAdapter.OnClickListener { topic ->
            Toast.makeText(requireContext(), "ok", Toast.LENGTH_SHORT).show()

            binding.listHorizontal.smoothScrollToPosition(adapter.itemCount - 2);*/

        // loadData()
    }
    private fun setQuestion() {
        // noVisibleView()
        val items = mQuestionsList
        val question = items!![mCurrentPosition - 1]

        if (mCurrentPosition == items.size) {
            binding.submitTheoryBtn.text = "Главное меню"

        } else {
            binding.submitTheoryBtn.text = "Дальше"
        }
        //    binding.tvProgress.text = "$mCurrentPosition/" + binding.progressBar.max

        for (i in 0 until items.count()) {
            Glide.with(requireContext()).load(question.imageDetail.toString())
                .transform(RoundedCorners(35))
                .into(binding.imageTheory)

            binding.titleTheory.text = question.question
            binding.theoryTranslate.text = question.correctWord
            binding.exampleTextTheory.text = question.exampleSentence
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submitTheoryBtn -> {
                mCurrentPosition++
                when {
                    mCurrentPosition <= mQuestionsList?.size!! -> {
                       setQuestion()
                    }
                    else -> {
                        findNavController().navigate(
                            R.id.action_theoryFragment_to_mainQuizFragment
                        )
                    }
                }
            }
            R.id.theory_speach -> {
                speakOut()
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts?.setLanguage(Locale.ENGLISH)

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
        Log.d(TAG, "speakOut: $text")
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }


    override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}