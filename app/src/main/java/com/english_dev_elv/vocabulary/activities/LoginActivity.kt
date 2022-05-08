package com.english_dev_elv.vocabulary.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.english_dev_elv.vocabulary.R
import com.english_dev_elv.vocabulary.databinding.ActivityLoginBinding
import com.english_dev_elv.vocabulary.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.enterButtonLogin.setOnClickListener {
            val email = binding.emailEdittextLogin.text.toString()
            val password = binding.passEdittextLogin.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {  }
        }

        binding.backToRegistrationTxt.setOnClickListener {
            finish()
        }
    }
}