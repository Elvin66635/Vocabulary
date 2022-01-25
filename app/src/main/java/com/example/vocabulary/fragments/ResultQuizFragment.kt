package com.example.vocabulary.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.vocabulary.R
import com.example.vocabulary.databinding.FragmentQuizResultBinding
import kotlinx.coroutines.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

private const val TAG = "ExampleDetailFragment"

class ResultQuizFragment : Fragment(), Animation.AnimationListener {
    private var binding: FragmentQuizResultBinding? = null
    private var totalQuestions: Int? = 0
    private var correctAnswer: Int? = 0
    private var savedResult: Int? = 0
    private var result: Int? = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val fragmentBinding = FragmentQuizResultBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        val anim = AnimationUtils.loadAnimation(context, R.anim.slide_down)
        anim.setAnimationListener(this)
        GlobalScope.launch(Dispatchers.Main) {
            val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.complete)
            mediaPlayer.start()
            congratulateKonfetti()
        }
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totalQuestions = arguments?.getInt("total_questions", 0)
        correctAnswer = arguments?.getInt("correct_answers", 0)
        val image = arguments?.getString("image")
        val title = arguments?.getString("title")
        savedResult = arguments?.getInt("saved_result")

        result = savedResult!! + correctAnswer!!
        Log.d(TAG, "onViewCreated: $result")

        val badResult = totalQuestions!! / 2
        val normalResult = totalQuestions!! / 1.5
        val goodResult = totalQuestions!! / 1.3
        val bestResult = totalQuestions!! / 1.1

        if (correctAnswer!! < normalResult) {
            binding!!.textResult.text = "Потренируйтесь ещё!"
            binding!!.amountResult.text = "$correctAnswer из $totalQuestions"
            binding!!.ratingBar.numStars = 1
            binding!!.ratingBar.setIsIndicator(true)
        } else if (correctAnswer!! < goodResult) {
            binding!!.textResult.text = "Хорошо!"
            binding!!.amountResult.text = "$correctAnswer из $totalQuestions"
            binding!!.ratingBar.numStars = 2
            binding!!.ratingBar.setIsIndicator(true)
        } else if (correctAnswer!! < badResult) {
            binding!!.textResult.text = "Плохой результат!"
            binding!!.amountResult.text = "$correctAnswer из $totalQuestions"
        } else {
            binding!!.textResult.text = "Отлично!"
            binding!!.amountResult.text = "$correctAnswer из $totalQuestions"
            binding!!.ratingBar.setIsIndicator(true)
            binding!!.ratingBar.numStars = 3
        }

        val bundle = Bundle()
        if (correctAnswer != null) {
            bundle.putInt("total_questions", totalQuestions!!)
        }

        Glide.with(requireContext()).load(image)
            .transform(RoundedCorners(35))
            .centerCrop()
            .into(binding!!.imageResult)

        binding!!.titleResult.text = title

        binding!!.goToMain.setOnClickListener {
            saveData()
            findNavController().navigate(
                R.id.action_exampleDetailFragment_to_mainQuizFragment,
                bundle
            )
        }
    }

    suspend fun congratulateKonfetti() {

        binding!!.konfettiView.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(10f, 15f)
            .setFadeOutEnabled(true)
            .addShapes(Shape.Square, Shape.Circle)
            .setTimeToLive(3000L)
            .addSizes(Size(10))
            .setPosition(-50f, binding!!.konfettiView.getWidth() + 50f, -50f, -50f)
            .streamFor(300, 1500L)
    }

    override fun onAnimationStart(p0: Animation?) {

    }

    override fun onAnimationEnd(p0: Animation?) {
    }

    override fun onAnimationRepeat(p0: Animation?) {
    }


    private fun saveData() {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = sharedPreferences.edit()
        editor.apply {
            this?.putInt("result", result!!)
        }?.apply()

    }
}