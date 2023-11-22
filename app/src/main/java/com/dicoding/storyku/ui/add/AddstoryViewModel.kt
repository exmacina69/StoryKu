package com.dicoding.storyku.ui.add

import androidx.lifecycle.ViewModel
import com.dicoding.storyku.data.repository.StoryRepository
import java.io.File

class AddstoryViewModel(private val repository: StoryRepository) : ViewModel() {

    fun uploadStories(file: File, description: String, lat: Double? = null, lon: Double? = null) =
        repository.uploadStories(file, description, lat, lon)
}