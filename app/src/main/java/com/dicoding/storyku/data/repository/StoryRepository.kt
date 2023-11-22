package com.dicoding.storyku.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyku.data.Hasil
import com.dicoding.storyku.data.api.ApiConfig
import com.dicoding.storyku.data.api.ApiService
import com.dicoding.storyku.data.database.StoryDataBase
import com.dicoding.storyku.data.database.StoryRemoteMediator
import com.dicoding.storyku.data.preference.ModelUser
import com.dicoding.storyku.data.preference.UserPreference
import com.dicoding.storyku.data.response.ErrorResponse
import com.dicoding.storyku.data.response.ListStoryResponse
import com.dicoding.storyku.data.response.LoginResponse
import com.dicoding.storyku.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val dataBase: StoryDataBase,
    private val apiService: ApiService,

    private val userPreference: UserPreference
) {

    private suspend fun saveSession(user: ModelUser) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<ModelUser> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(name: String, email: String, password: String): Hasil<RegisterResponse> {
        Hasil.Loading
        return try {
            val response = apiService.register(name, email, password)

            if (response.error == true) {
                Hasil.Error(response.message ?: "Unknown error")
            } else {
                Hasil.Success(response)
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Hasil.Error(errorMessage.toString())
        }
    }

    suspend fun login(email: String, password: String): Hasil<LoginResponse> {
        Hasil.Loading
        return try {
            val response = apiService.login(email, password)

            if (response.error == true) {
                Hasil.Error(response.message)
            } else {
                val session = ModelUser(
                    name = response.loginResult.name,
                    email = email,
                    token = response.loginResult.token,
                    isLogin = true
                )
                saveSession(session)
                ApiConfig.token = response.loginResult.token
                Hasil.Success(response)
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Hasil.Error(errorMessage.toString())
        }
    }

    fun uploadStories(imageFile: File, description: String, lat: Double?, lon: Double?) = liveData {
        emit(Hasil.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        val requestLat = lat?.toString()?.toRequestBody()
        val requestLon = lon?.toString()?.toRequestBody()
        try {
            val successResponse = apiService.uploadImage(multipartBody, requestBody, requestLat, requestLon)
            if (successResponse.error) {
                emit(Hasil.Error(successResponse.message))
            } else {
                emit(Hasil.Success(successResponse))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Hasil.Error(errorMessage.toString()))
        }
    }

    fun getStoriesWithLocation(): LiveData<Hasil<List<ListStoryResponse>>> = liveData {
        emit(Hasil.Loading)
        try {
            val response = apiService.getStoriesWithLocation()
            emit(Hasil.Success(response.listStory))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Hasil.Error(errorMessage.toString())
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<ListStoryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(dataBase, apiService),
            pagingSourceFactory = {
                dataBase.storyDao().getAllStory()
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            dataBase: StoryDataBase,
            apiService: ApiService,
            userPreference: UserPreference
        ): StoryRepository = StoryRepository(dataBase, apiService, userPreference)
    }
}