package com.english_dev_elv.vocabulary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.english_dev_elv.vocabulary.databinding.CustomRowBinding
import com.english_dev_elv.vocabulary.model.Topic

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
        }
    }

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

    class OnClickListener(val clickListener: (topic: Topic) -> Unit) {
        fun onClick(topic: Topic) = clickListener(topic)
    }
}