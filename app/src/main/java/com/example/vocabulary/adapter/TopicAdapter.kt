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
import com.example.vocabulary.model.Topic

private const val TAG = "TopicAdapter"

class TopicAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Topic, TopicAdapter.TopicViewHolder>(MyDiffUtil) {

    companion object MyDiffUtil : DiffUtil.ItemCallback<Topic>() {
        override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem == newItem
        }
    }

    class TopicViewHolder(private val binding: CustomRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(topic: Topic) {
            binding.articleTitle.text = topic.title
            Glide.with(binding.articleImg).load(topic.image).into(binding.articleImg)
            binding.articleTitle.text = topic.title
        }
    }

    var topics = mutableListOf<Topic>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(
            CustomRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(topic)
        }
        holder.bind(topic)
    }

/*
    fun setTopicList(topic: List<Topic>) {
        this.topics = topic.toMutableList()
        notifyDataSetChanged()
    }*/

    class OnClickListener(val clickListener: (topic: Topic) -> Unit) {
        fun onClick(topic: Topic) = clickListener(topic)
    }
}