package com.dicoding.picodiploma.loginwithanimation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.databinding.LoadingStateBinding

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val binding = LoadingStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadingStateViewHolder(private val binding: LoadingStateBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.refreshbtn.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.loading.isVisible = loadState is LoadState.Loading
            binding.loading.isVisible = loadState is LoadState.Error
        }
    }
}