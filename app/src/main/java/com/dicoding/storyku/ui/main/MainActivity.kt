package com.dicoding.storyku.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyku.R
import com.dicoding.storyku.databinding.ActivityMainBinding
import com.dicoding.storyku.ui.add.AddstoryActivity
import com.dicoding.storyku.ui.maps.MapsActivity
import com.dicoding.storyku.ui.welcome.WelcomeActivity
import com.dicoding.storyku.utils.NotFoundStoryAdapter
import com.dicoding.storyku.utils.ViewModelFactory
import androidx.appcompat.app.ActionBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val storyAdapter = MainAdapter()

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        binding.rvUser.adapter = storyAdapter.withLoadStateFooter(
            footer = NotFoundStoryAdapter {
                storyAdapter.retry()
            }
        )
    }

    private fun setUpAction() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddstoryActivity::class.java))
            finish()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.action_bar)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu2 -> {
                startActivity(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.menu1 -> {
                viewModel.logout()
                return true
            }
            R.id.menu3 -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAction()
        setupActionBar()
        setupRecyclerView()

        viewModel.getSession().observe(this) { user ->
            Log.d("token", "onCreate: ${user.token}")
            Log.d("user", "onCreate: ${user.isLogin}")
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        viewModel.story.observe(this) { story ->
            storyAdapter.submitData(lifecycle, story)
        }
    }
}
