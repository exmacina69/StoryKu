package com.dicoding.storyku.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyku.data.repository.StoryRepository
import com.dicoding.storyku.data.preference.ModelUser
import com.dicoding.storyku.data.response.ListStoryResponse
import kotlinx.coroutines.launch


class MainViewModel(private val repository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<ListStoryResponse>> = repository.getStories().cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<ModelUser> {
        return repository.getSession().asLiveData()
    }
}