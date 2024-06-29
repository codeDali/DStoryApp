package com.dicoding.picodiploma.loginwithanimation.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesItem
import com.dicoding.picodiploma.loginwithanimation.databinding.RvItemBinding
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailActivity

class StoriesAdapter: PagingDataAdapter<StoriesItem, StoriesAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder (val binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoriesItem) {
            binding.tvUsername.text = story.name
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.ivStories)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        story?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("id", story?.id)
            val imagePair = android.util.Pair.create(holder.binding.ivStories as View, "image")
            val titlePair = android.util.Pair.create(holder.binding.tvUsername as View, "title")

            val option = ActivityOptions.makeSceneTransitionAnimation(
                holder.itemView.context as Activity,
                imagePair,
                titlePair
            ).toBundle()

            holder.itemView.context.startActivity(intent, option)
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoriesItem>() {
            override fun areItemsTheSame(oldItem: StoriesItem, newItem: StoriesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoriesItem, newItem: StoriesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}