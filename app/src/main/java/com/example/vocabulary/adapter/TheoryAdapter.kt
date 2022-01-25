package com.example.vocabulary.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vocabulary.R
import com.example.vocabulary.databinding.CustomRowBinding
import com.example.vocabulary.databinding.ItemTheoryBinding
import com.example.vocabulary.model.QuizDetails
import com.example.vocabulary.model.Topic

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