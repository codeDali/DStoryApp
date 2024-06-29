package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.test.espresso.idling.CountingIdlingResource
import com.dicoding.picodiploma.loginwithanimation.helper.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesItem
import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    val stories: LiveData<PagingData<StoriesItem>> = repository.getStories().cachedIn(viewModelScope)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val idlingResource = CountingIdlingResource("Logout")


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

//    fun getStories() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val response = repository.getStory()
//                _stories.value = response
//            } catch (e: HttpException) {
//                val errorBody = e.response()?.errorBody()?.string()
//                if (errorBody != null && errorBody.startsWith("<html>")) {
//                    _stories.value = StoriesResponse(error = true, message = "Received HTML response instead of JSON")
//                } else {
//                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
//                    val errorMessage = errorResponse.message
//                    _stories.value = StoriesResponse(error = true, message = errorMessage)
//                }
//            } catch (e: Exception) {
//                _stories.value = StoriesResponse(error = true, message = "An error occurred: ${e.message}")
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    fun logout() {
        idlingResource.increment()
        try {
            viewModelScope.launch {
                repository.logout()
            }
        } finally {
            idlingResource.decrement()
        }

    }

}