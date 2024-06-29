package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.SignUpResponse
import com.dicoding.picodiploma.loginwithanimation.helper.UserRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel (private val userRepository: UserRepository) : ViewModel() {
    private val _signUpResult = MutableLiveData<SignUpResponse>()
    val signUpResult: LiveData<SignUpResponse> = _signUpResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signUp(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _signUpResult.value = userRepository.signUp(name, email, password)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _signUpResult.value = SignUpResponse(error = true, message = errorMessage)
            } finally {
                _isLoading.value = false
            }
        }
    }
}