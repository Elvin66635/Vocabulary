package com.example.vocabulary.fragments

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_exampleDetailFragment_to_mainQuizFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentQuizResultBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        val anim = AnimationUtils.loadAnimation(context, R.anim.slide_down)
        anim.setAnimationListener(this)
        GlobalScope.launch (Dispatchers.Main) {
            val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.complete)
            mediaPlayer.start()
            congratulateKonfetti()
        }
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        val totalQuestions = arguments?.getInt("total_questions", 0)
        val correctAnswer = arguments?.getInt("correct_answers", 0)
        val image = arguments?.getString("image")
        val title = arguments?.getString("title")
        Log.d(TAG, "onViewCreated: $image")

        val badResult = totalQuestions!! / 2
        val normalResult = totalQuestions!! / 1.5
        val goodResult = totalQuestions!! / 1.3
        val bestResult = totalQuestions!! / 1.1

        if (correctAnswer!! < normalResult) {
            binding!!.textResult.text = "Потренируйтесь ещё!"
            binding!!.amountResult.text = "$correctAnswer из $totalQuestions"
        } else if (correctAnswer < bestResult) {
            binding!!.textResult.text = "Хорошо!"
            binding!!.amountResult.text = "$correctAnswer из $totalQuestions"
        } else if (correctAnswer < badResult){
            binding!!.textResult.text = "Плохой результат!"
            binding!!.amountResult.text = "$correctAnswer из $totalQuestions"
        }else {
            binding!!.textResult.text = "Отлично!"
            binding!!.amountResult.text = "$correctAnswer из $totalQuestions"
        }

        val bundle = Bundle()
        if (correctAnswer != null) {
            bundle.putInt("correct_answers", correctAnswer)
        }

        Glide.with(requireContext()).load(image)
            .transform(RoundedCorners(35))
            .centerCrop()
            .into(binding!!.imageResult)

        binding!!.titleResult.text = title




    //       binding?.resultTxt?.text = "Your score is $correctAnswer out of $totalQuestions"
    Log.d(TAG, "onViewCreated: $binding?.resultTxt?.text = \"Your score is $correctAnswer out of $totalQuestions\"")
    //  findNavController().navigate(R.id.action_exampleDetailFragment_to_mainQuizFragment, bundle)
}

    suspend fun congratulateKonfetti()  {

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



}