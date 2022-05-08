package com.english_dev_elv.vocabulary

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.english_dev_elv.vocabulary.activities.PhoneticsDetailsActivity
import com.english_dev_elv.vocabulary.adapter.PhoneticsAdapter
import com.english_dev_elv.vocabulary.adapter.TopicAdapter
import com.english_dev_elv.vocabulary.databinding.ActivityPhoneticsBinding
import com.english_dev_elv.vocabulary.databinding.FragmentMainNounsBinding
import com.english_dev_elv.vocabulary.fragments.isNetWorkAvailable
import com.english_dev_elv.vocabulary.fragments.netInterceptor
import com.english_dev_elv.vocabulary.network.API
import com.english_dev_elv.vocabulary.repository.TopicsRepository
import com.english_dev_elv.vocabulary.viewmodel.TopicViewModel
import com.english_dev_elv.vocabulary.viewmodel.TopicViewModelFactory
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

private const val TAG = "PhoneticsActivity"

class PhoneticsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPhoneticsBinding
    lateinit var viewModel: TopicViewModel
    private lateinit var adapter: PhoneticsAdapter
    private val retrofitService = API.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneticsBinding.inflate(layoutInflater)
        val view = binding.root
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(view)
        initToolbar()

        binding.recyclerPhonetics.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = PhoneticsAdapter(PhoneticsAdapter.OnClickListener { topic ->

            val intent = Intent(this, PhoneticsDetailsActivity::class.java)
            intent.putExtra("letter", topic.letter)
            intent.putExtra("letter_description", topic.letter_description)

          /*  var items = topic.example_words
            for (i in 0 until topic.example_words.count()) {
                val details = items[i].word
                intent.putExtra("word", details)
                Log.d(TAG, "onCreate: ${details.toString()}")
            }*/
            intent.putParcelableArrayListExtra("list", topic.example_words)
            startActivity(intent)
        })

        loadData()

    }

    private fun loadData() {

        viewModel =
            ViewModelProvider(this, TopicViewModelFactory(TopicsRepository(retrofitService)))
                .get(TopicViewModel::class.java)

        viewModel.phonetics.observe(this, {
            binding.recyclerPhonetics.adapter = adapter
            Log.d(TAG, "loadData: ${adapter.submitList(it)}")

        })

        viewModel.errorMessage.observe(this, {
            Log.d(TAG, "Error $it")
        })

        viewModel.getPhonetics()
    }


    private fun initToolbar() {
        val actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.title = "Фонетика"
        val colorDrawable = ColorDrawable(Color.parseColor("#A1624B5C"))
        actionBar?.setBackgroundDrawable(colorDrawable)
    }
}