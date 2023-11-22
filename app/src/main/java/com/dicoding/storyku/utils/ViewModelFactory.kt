package com.dicoding.storyku.utils


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyku.data.repository.StoryRepository
import com.dicoding.storyku.di.Injection
import com.dicoding.storyku.ui.add.AddstoryViewModel
import com.dicoding.storyku.ui.login.SigninViewModel
import com.dicoding.storyku.ui.main.MainViewModel
import com.dicoding.storyku.ui.maps.MapsViewModel
import com.dicoding.storyku.ui.register.SignupViewModel


class ViewModelFactory(private val repository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return ViewModelFactory(Injection.provideRepository(context))
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(AddstoryViewModel::class.java) -> {
                AddstoryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}