package com.dicoding.picodiploma.loginwithanimation.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesResponse
import com.dicoding.picodiploma.loginwithanimation.helper.UserRepository
import kotlinx.coroutines.launch

class MapsViewModel (private val repository: UserRepository): ViewModel(){

    private val _storiesLoc = MutableLiveData<StoriesResponse>()
    val storiesLoc: LiveData<StoriesResponse> = _storiesLoc

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getStoriesLoc(){
        viewModelScope.launch {
            try {
                val response= repository.getStoriesLocation()
                Log.d("MapsViewModel", "Fetched stories: ${response.listStory}")
                _storiesLoc.postValue(response)
            } catch (e: Exception) {
                _error.value = "Error fetching stories: ${e.message}"
            }

        }
    }

}