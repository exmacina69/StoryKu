package com.dicoding.storyku.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.dicoding.storyku.R
import com.dicoding.storyku.data.Hasil
import com.dicoding.storyku.data.response.RegisterResponse
import com.dicoding.storyku.databinding.ActivityRegisterBinding
import com.dicoding.storyku.utils.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val register = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.tvTittle, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(100)
        val etlName = ObjectAnimator.ofFloat(binding.edlayoutName, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(100)
        val etlEmail = ObjectAnimator.ofFloat(binding.edlayoutEmail, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(100)
        val etlPassword = ObjectAnimator.ofFloat(binding.edlayoutPassword, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, name, etlName, email, etlEmail, password, etlPassword, register)
            start()
        }
    }
    private fun setupAction() {
        binding.btnSignup.setOnClickListener {
            val name = binding.edName.text.toString()
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            viewModel.register(name, email, password)
            viewModel.registrationResult.observe(this) { result ->
                when (result) {
                    is Hasil.Loading -> {
                        showLoading(true)
                    }

                    is Hasil.Success -> {
                        showLoading(false)
                        val response: RegisterResponse = result.data
                        AlertDialog.Builder(this).apply {
                            setTitle("Mantap")
                            setMessage(response.message)
                            setPositiveButton("Next") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }

                    is Hasil.Error -> {
                        showLoading(false)
                        val errorMessage: String = result.error
                        AlertDialog.Builder(this).apply {
                            setTitle("Waduh")
                            setMessage(errorMessage)
                            setPositiveButton("OKE") { _, _ ->
                            }
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                pbRegister.visibility = View.VISIBLE
            } else {
                pbRegister.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupAction()

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.action_bar)
    }
}