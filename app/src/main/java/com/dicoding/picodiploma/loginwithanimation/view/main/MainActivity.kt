package com.dicoding.picodiploma.loginwithanimation.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.adapter.LoadingStateAdapter
import com.dicoding.picodiploma.loginwithanimation.adapter.StoriesAdapter
import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesResponse
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.maps.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.view.upload.UploadActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        binding.root?.let { rootView ->

            ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }


        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.fabAdd.setOnClickListener{
            Intent(this, UploadActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.fabLogout.setOnClickListener{
            AlertDialog.Builder(this)
                .setMessage("Anda akan Logout")
                .setPositiveButton("Ya") { dialog, _ ->
                    viewModel.logout()
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    dialog.dismiss()
                    finish()
                }
                .setNegativeButton("Tetap Disini") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_open_map -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        setStories()
        setupView()
        playAnimation()
    }


    private fun setStories() {
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        adapter = StoriesAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.stories.observe(this) {pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                showLoading(true)
            } else {
                showLoading(false)
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        val topAppBar = ObjectAnimator.ofFloat(binding.topAppBar, View.ALPHA, 1f).setDuration(100)
        val rvStories = ObjectAnimator.ofFloat(binding.rvStories, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(topAppBar, rvStories)
            startDelay = 100
        }.start()
    }
}
