package com.dicoding.storyku.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyku.data.repository.StoryRepository
import com.dicoding.storyku.data.Hasil
import com.dicoding.storyku.data.response.ListStoryResponse

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getStoriesWithLocation(): LiveData<Hasil<List<ListStoryResponse>>> {
        return repository.getStoriesWithLocation()
    }
}