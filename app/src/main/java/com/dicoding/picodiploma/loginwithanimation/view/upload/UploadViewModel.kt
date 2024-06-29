package com.dicoding.picodiploma.loginwithanimation.view.upload

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.response.UploadResponse
import com.dicoding.picodiploma.loginwithanimation.helper.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val repository: UserRepository) : ViewModel() {

    private val uploadResult = MutableLiveData<UploadResponse>()

    fun uploadStory(imagePart: MultipartBody.Part, descriptionPart: RequestBody) {
        viewModelScope.launch {
            try {
                val response = repository.uploadStory(imagePart, descriptionPart)
                uploadResult.value = response
            } catch (e: Exception) {
                uploadResult.value = UploadResponse(error = true, message = e.message)
            }
        }
    }

    fun uploadStory(imagePart: MultipartBody.Part, descriptionPart: RequestBody, latitude: RequestBody, longitude: RequestBody) {
        viewModelScope.launch {
            try {
                val response = repository.uploadStory(imagePart, descriptionPart, latitude, longitude)
                uploadResult.value = response
            } catch (e: Exception) {
                uploadResult.value = UploadResponse(error = true, message = e.message)
            }
        }
    }
}
