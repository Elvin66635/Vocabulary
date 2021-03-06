package com.english_dev_elv.vocabulary.fragments

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.res.Resources
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.english_dev_elv.vocabulary.R
import com.english_dev_elv.vocabulary.databinding.FragmentTheoryBinding
import com.english_dev_elv.vocabulary.model.QuizDetails
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "TheoryFragment"

class TheoryFragment : Fragment(), View.OnClickListener, TextToSpeech.OnInitListener {

    private lateinit var binding: FragmentTheoryBinding
    private var mQuestionsList: ArrayList<QuizDetails>? = null
    private var tts: TextToSpeech? = null
    private var mCurrentPosition: Int = 1
    private var isCheck = true

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
        binding.exampleTextTheory.setOnClickListener(this)
        binding.translateImage.setOnClickListener(this)
        setQuestion()


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val exitBuilderDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
                exitBuilderDialog.setTitle("??????????")
                exitBuilderDialog.setMessage("???? ?????????????????????????? ???????????? ???????????")
                exitBuilderDialog.setIcon(R.drawable.ic_exit_dialog)
                exitBuilderDialog.setPositiveButton("????"){
                        Dialog, which->

                    findNavController().navigate(
                        R.id.action_theoryFragment_to_mainQuizFragment)
                }

                exitBuilderDialog.setNegativeButton("??????"){
                        Dialog, which->
                }
                val createBuild = exitBuilderDialog.create()
                createBuild.show()
            }
        })

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
        hideTranslateText()
        val items = mQuestionsList
        val question = items!![mCurrentPosition - 1]

        if (mCurrentPosition == items.size) {
            binding.submitTheoryBtn.text = "?????????????? ????????"

        } else {
            binding.submitTheoryBtn.text = "????????????"
        }

        for (i in 0 until items.count()) {
            Glide.with(requireContext()).load(question.imageDetail.toString())
                .transform(RoundedCorners(35))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.loading_placeholder)
                .into(binding.imageTheory)

            binding.titleTheory.text = question.question
            binding.theoryTranslate.text = question.correctWord
            binding.exampleTextTheory.text = question.exampleSentence
            binding.translateTextTheory.text = question.translateSentence
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submitTheoryBtn -> {
                mCurrentPosition++
                val animation = AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.fade_in
                )
                binding.constraintTheory.startAnimation(animation)
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
            R.id.example_text_theory -> {
                if (!binding.translateTextTheory.isVisible) {
                    showTranslateText()
                } else {
                    hideTranslateText()
                }
            }
            R.id.translate_image -> {
                if (!binding.translateTextTheory.isVisible) {
                    showTranslateText()
                } else {
                    hideTranslateText()
                }
            }
        }
    }

    private fun showTranslateText() {
        binding.translateTextTheory.updateHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT)

        val totalMarginForSubtitle = 2 * 16.toPx()
        binding.translateTextTheory.measure(
            View.MeasureSpec.makeMeasureSpec(
                binding.constraintTheory.width - totalMarginForSubtitle,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.UNSPECIFIED
        )
        val subtitleHeight = binding.translateTextTheory.measuredHeight

        binding.translateTextTheory.height = 0
        binding.translateTextTheory.isVisible = true
        val heightAnimator = ValueAnimator.ofInt(0, subtitleHeight)
        heightAnimator.addUpdateListener {
            binding.translateTextTheory.height = it.animatedValue as Int
        }
        heightAnimator.start()
    }

    private fun hideTranslateText() {
      //  binding.translateTextTheory.visibility = View.GONE

        val subtitleHeight = binding.translateTextTheory.height
        val heightAnimator = ValueAnimator.ofInt(subtitleHeight, 0)

        heightAnimator.addUpdateListener {
            binding.translateTextTheory.updateHeight(it.animatedValue as Int)
        }
        heightAnimator.doOnEnd {
            binding.translateTextTheory.isVisible = false
        }
        heightAnimator.start()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.ENGLISH)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            } else {
                Log.e("TTS", "Ok")
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
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
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


fun View.updateHeight(newHeight: Int) {
    layoutParams = layoutParams.apply {
        height = newHeight
    }
}


fun Int.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
).toInt()
