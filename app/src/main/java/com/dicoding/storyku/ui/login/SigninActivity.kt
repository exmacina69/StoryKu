package com.dicoding.storyku.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.dicoding.storyku.R
import com.dicoding.storyku.data.response.LoginResponse
import com.dicoding.storyku.databinding.ActivityLoginBinding
import com.dicoding.storyku.utils.ViewModelFactory
import com.dicoding.storyku.ui.main.MainActivity
import com.dicoding.storyku.data.Hasil


class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<SigninViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private fun showErrorMessage(errorMessage: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Waduh")
            setMessage(errorMessage)
            setPositiveButton("OK") { _, _ -> }
            create()
            show()
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val password = binding.edPassword.text.toString()
            val email = binding.edEmail.text.toString()

            viewModel.loginResult.observe(this) { result ->
                showLoading(false)
                when (result) {
                    is Hasil.Error -> showErrorMessage(result.error)
                    is Hasil.Success -> showSuccessMessage(result.data)
                    is Hasil.Loading -> showLoading(true)
                }
            }
            viewModel.login(email, password)
        }
    }

    private fun playAnimation() {
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.tvTittle, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.tvTagline, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(100)
        val etlEmail = ObjectAnimator.ofFloat(binding.edlayoutEmail, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(100)
        val etlPassword = ObjectAnimator.ofFloat(binding.edlayoutPassword, View.ALPHA, 1f).setDuration(100)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(title, message, email, etlEmail, password, etlPassword, login)
        animatorSet.start()

        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun showSuccessMessage(response: LoginResponse) {
        AlertDialog.Builder(this).apply {
            setTitle("Mantap")
            setMessage(response.message)
            setPositiveButton("OK") { _, _ ->
                val intent = Intent(this@SigninActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.pbLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.action_bar)
        }

        playAnimation()
        setupAction()
    }
}
