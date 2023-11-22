package com.dicoding.storyku.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.storyku.R
import com.dicoding.storyku.databinding.ActivityDetailstoryBinding
import com.dicoding.storyku.utils.ViewModelFactory
import com.dicoding.storyku.ui.main.MainViewModel

class DetailstoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailstoryBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private fun setCustomActionBar() {
        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.action_bar)
        }
    }

    private fun bindData(name: String, description: String?, picture: String?) {
        binding.tvDetailName.text = name
        binding.tvDetailDescription.text = description
        Glide.with(this).load(picture).into(binding.ivDetailPhoto)
    }

    companion object {
        const val ID = "ID"
        const val NAME = "NAME"
        const val DESCRIPTION = "DESCRIPTION"
        const val PICTURE = "PICTURE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomActionBar()
        binding = ActivityDetailstoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(ID) ?: ""
        val name = intent.getStringExtra(NAME) ?: ""
        val description = intent.getStringExtra(DESCRIPTION)
        val picture = intent.getStringExtra(PICTURE)

        bindData(name, description, picture)
    }


}
