package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.espresso.idling.CountingIdlingResource
import com.dicoding.picodiploma.loginwithanimation.helper.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class   LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _errorMesssage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMesssage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val idlingResource = CountingIdlingResource("Login")


    fun login(email: String, password: String) {
        _isLoading.value = true
        idlingResource.increment()
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                _loginResult.value = response
                if (response.error == true) {
                    _errorMesssage.value = response.message
                    _isLoading.value = false
                    response.loginResult?.let { loginresult ->
                        val userModel = UserModel(
                            email = email,
                            token = loginresult.token ?:"",
                            isLogin = true
                        )
                        repository.saveSession(userModel)
                    }
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = if (errorBody.isNullOrEmpty()) {
                    "Unknown error"
                } else {
                    try {
                        Gson().fromJson(errorBody, ErrorResponse::class.java).message
                    } catch (jsonException: Exception) {
                        "Error parsing response"
                    }
                }
                _loginResult.value = LoginResponse(error = true, message = errorMessage)
            } finally {
                _isLoading.value = false
                idlingResource.decrement()
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}