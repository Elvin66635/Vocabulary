package com.example.vocabulary.Nouns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.vocabulary.R
import com.example.vocabulary.databinding.ActivityNounsBinding

class NounsVocabulary : AppCompatActivity() {

    lateinit var binding: ActivityNounsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNounsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar!!.hide()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController)


    }

 /*   override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }*/
}