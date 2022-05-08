package com.english_dev_elv.vocabulary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.english_dev_elv.vocabulary.databinding.ItemPhoneticsBinding
import com.english_dev_elv.vocabulary.model.Phonetic

private const val TAG = "PhoneticsAdapter"

class PhoneticsAdapter constructor(private val onClickListener: OnClickListener) :
    ListAdapter<Phonetic, PhoneticsAdapter.TopicViewHolder>(MyDiffUtil) {


    companion object MyDiffUtil : DiffUtil.ItemCallback<Phonetic>() {
        override fun areItemsTheSame(oldItem: Phonetic, newItem: Phonetic): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Phonetic, newItem: Phonetic): Boolean {
            return oldItem == newItem
        }
    }

    class TopicViewHolder(private val binding: ItemPhoneticsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(topic: Phonetic) {
            binding.letter.text = topic.letter
            Log.d(TAG, "bind: " + binding.letter.text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(
            ItemPhoneticsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    class OnClickListener(val clickListener: (topic: Phonetic) -> Unit) {
        fun onClick(topic: Phonetic) = clickListener(topic)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(topic)
        }
        holder.bind(topic)
    }
}