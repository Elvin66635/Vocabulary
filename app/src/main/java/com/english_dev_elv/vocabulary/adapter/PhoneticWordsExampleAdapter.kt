package com.english_dev_elv.vocabulary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.english_dev_elv.vocabulary.databinding.ItemPhoneticsBinding
import com.english_dev_elv.vocabulary.databinding.PhoneticWordsExampleBinding
import com.english_dev_elv.vocabulary.model.Phonetic
import com.english_dev_elv.vocabulary.model.QuizDetails
import com.english_dev_elv.vocabulary.model.WordsExamplePhonetics

class PhoneticWordsExampleAdapter(private val onClickListener: OnClickListener, private val data: ArrayList<WordsExamplePhonetics>) :
    RecyclerView.Adapter<PhoneticWordsExampleAdapter.TopicViewHolder>() {

    class TopicViewHolder(private val binding: PhoneticWordsExampleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var wordOriginal = binding.wordOriginal
        var wordTranscription = binding.wordTranscription
        var wordTranslation = binding.wordTranslation

        var imageView = binding.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(
            PhoneticWordsExampleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = data[position]
        holder.wordOriginal.text = data[position].word
        holder.wordTranslation.text = data[position].translation
        holder.wordTranscription.text = data[position].transcription


        holder.imageView.setOnClickListener {
            onClickListener.onClick(topic)
        }

}

    class OnClickListener(val clickListener: (topic: WordsExamplePhonetics) -> Unit) {
        fun onClick(topic: WordsExamplePhonetics) = clickListener(topic)
    }
}
