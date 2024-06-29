package com.dicoding.picodiploma.loginwithanimation.data.retrofit

import com.dicoding.picodiploma.loginwithanimation.data.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.SignUpResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignUpResponse

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoriesResponse

    @GET("stories/{id}")
    suspend fun getStoryId(@Path("id") id: String): DetailStoryResponse

    @GET("stories")
    suspend fun getStoriesLocation(
        @Query("location") location : Int = 1,
    ): StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("latitude") latitude: RequestBody? = null,
        @Part("longitude") longitude: RequestBody? = null
    ): UploadResponse
}