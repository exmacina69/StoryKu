package com.dicoding.storyku.ui.register


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyku.data.repository.StoryRepository
import com.dicoding.storyku.data.response.RegisterResponse
import kotlinx.coroutines.launch
import com.dicoding.storyku.data.Hasil

class SignupViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _registrationResult = MutableLiveData<Hasil<RegisterResponse>>()
    val registrationResult: LiveData<Hasil<RegisterResponse>> get() = _registrationResult

    fun register(name: String, email: String, password: String) {
        _registrationResult.value = Hasil.Loading

        viewModelScope.launch {
            val result = storyRepository.register(name, email, password)
            _registrationResult.value = result
        }
    }
}