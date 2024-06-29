package com.dicoding.picodiploma.loginwithanimation.di

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.data.db.StoriesDatabase
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiClient
import com.dicoding.picodiploma.loginwithanimation.helper.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object InjectionModule {
    fun provideRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context)
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiClient.getApiService(user.token)
        val storyDatabase = StoriesDatabase.getDatabase(context)
        return UserRepository(userPreference, apiService, storyDatabase)
    }
}