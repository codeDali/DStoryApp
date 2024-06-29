package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.helper.UserRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailViewModel (private val userRepository: UserRepository) : ViewModel() {
    private val _detailstory = MutableLiveData<DetailStoryResponse>()
    val detailstory: LiveData<DetailStoryResponse> = _detailstory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoryId(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = userRepository.getStoryId(id)
                _detailstory.value = response
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                if (errorBody != null && errorBody.startsWith("<html>")) {
                    _detailstory.value = DetailStoryResponse(error = true, message = "Received HTML response instead of JSON")
                } else {
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    val errorMessage = errorResponse.message
                    _detailstory.value = DetailStoryResponse(error = true, message = errorMessage)
                }
            } catch (e: Exception) {
                _detailstory.value = DetailStoryResponse(error = true, message = "An error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}