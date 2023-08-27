package com.codewithnazeefo.mynewsapp

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.codewithnazeefo.mynewsapp.Repository.NewsRepository
import com.codewithnazeefo.mynewsapp.databinding.ActivityMainBinding
import com.codewithnazeefo.mynewsapp.db.ArticleDatabase
import com.codewithnazeefo.mynewsapp.ui.NewsModelProviderFactory
import com.codewithnazeefo.mynewsapp.ui.NewsViewModel

class MainActivity : AppCompatActivity() {
       lateinit var navController :NavController
       lateinit var viewModel: NewsViewModel

     private val binding :ActivityMainBinding by lazy {
         ActivityMainBinding.inflate(layoutInflater)
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)


        // initialized viewModel
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val newsModelProviderFactory = NewsModelProviderFactory(application , newsRepository)
        viewModel = ViewModelProvider(this,newsModelProviderFactory)[NewsViewModel::class.java]


//         binding.bottomNavView.setupWithNavController(binding.navHostFragment.findNavController())

    }
}