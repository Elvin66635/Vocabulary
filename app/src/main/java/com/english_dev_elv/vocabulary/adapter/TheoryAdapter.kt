package com.english_dev_elv.vocabulary.adapter

import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.english_dev_elv.vocabulary.databinding.ItemTheoryBinding
import com.english_dev_elv.vocabulary.model.QuizDetails

private const val TAG = "TopicAdapter"

class TheoryAdapter constructor(private val onClickListener: OnClickListener) :
    ListAdapter<QuizDetails, TheoryAdapter.TopicViewHolder>(MyDiffUtil) {


    companion object MyDiffUtil : DiffUtil.ItemCallback<QuizDetails>() {
        override fun areItemsTheSame(oldItem: QuizDetails, newItem: QuizDetails): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: QuizDetails, newItem: QuizDetails): Boolean {
            return oldItem == newItem
        }
    }

    class TopicViewHolder(private val binding: ItemTheoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(topic: QuizDetails) {
            binding.titleTheory.text = topic.question
        //    Glide.with(binding.imageTheory).load(topic.image).into(binding.imageTheory)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(
            ItemTheoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    class OnClickListener(val clickListener: (topic: QuizDetails) -> Unit) {
        fun onClick(topic: QuizDetails) = clickListener(topic)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(topic)
        }
        holder.bind(topic)
    }
}