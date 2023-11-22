package com.dicoding.storyku.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyku.data.repository.StoryRepository
import com.dicoding.storyku.data.response.LoginResponse
import kotlinx.coroutines.launch
import com.dicoding.storyku.data.Hasil


class SigninViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun login(email: String, password: String) {
        _loginResult.value = Hasil.Loading

        viewModelScope.launch {
            val result = storyRepository.login(email, password)
            _loginResult.value = result
        }
    }
    val loginResult: LiveData<Hasil<LoginResponse>> get() = _loginResult
    private val _loginResult = MutableLiveData<Hasil<LoginResponse>>()
}