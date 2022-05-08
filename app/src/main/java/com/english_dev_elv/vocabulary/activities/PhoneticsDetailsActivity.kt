package com.english_dev_elv.vocabulary.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.os.Bundle
import com.english_dev_elv.vocabulary.databinding.ActivityPhoneticsDetailBinding
import android.graphics.drawable.ColorDrawable
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.english_dev_elv.vocabulary.adapter.PhoneticWordsExampleAdapter
import com.english_dev_elv.vocabulary.adapter.PhoneticsAdapter
import com.english_dev_elv.vocabulary.model.WordsExamplePhonetics
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "PhoneticsDetailsActivity"

class PhoneticsDetailsActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityPhoneticsDetailBinding
    private var wordList: ArrayList<WordsExamplePhonetics>? = null
    private lateinit var letterDesc: String
    private lateinit var letter: String
    private lateinit var word: String
    private var adapter: PhoneticWordsExampleAdapter? = null
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneticsDetailBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)
        initToolbar()

        tts = TextToSpeech(this, this)

        letter = intent.getStringExtra("letter").toString()
        binding.title.text = letter
        letterDesc = intent.getStringExtra("letter_description").toString()
        binding.desc.text = letterDesc
        wordList = intent.getParcelableArrayListExtra("list")

        binding.wordList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = wordList?.let {
            PhoneticWordsExampleAdapter(PhoneticWordsExampleAdapter.OnClickListener { topic ->
                word = topic.word!!
                speakOut()
            }, it)
        }

        binding.wordList.adapter = adapter
    }

    private fun initToolbar() {
        val actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.title = "Фонетика"
        val colorDrawable = ColorDrawable(Color.parseColor("#A1624B5C"))
        actionBar?.setBackgroundDrawable(colorDrawable)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.UK)

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
            tts!!.speak(word, TextToSpeech.QUEUE_FLUSH, null, "")
        }
}