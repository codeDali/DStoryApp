package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val Id = intent.getStringExtra(EXTRA_STORY_ID)
        if (Id != null) {
            viewModel.getStoryId(Id)
        }

        viewModel.detailstory.observe(this) {DetailResponse ->
            fetchStories(DetailResponse)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.placeholder.text = "Deskripsi"
    }

    private fun showLoading(it: Boolean) {
        binding.loading.visibility = if (it) View.VISIBLE else View.GONE
    }

    private fun fetchStories(detailStoryResponse: DetailStoryResponse) {
        detailStoryResponse.story?.let { story ->
            binding.apply {
                tvusernameDetail.text = story.name
                tvDescriptionDetail.text = story.description
                tvTimeDetail.text = story.createdAt
            }
            Glide.with(this)
                .load(story.photoUrl)
                .into(binding.ivDetail)
        }
    }

    companion object {
        const val EXTRA_STORY_ID = "extra_story_id"
    }
}