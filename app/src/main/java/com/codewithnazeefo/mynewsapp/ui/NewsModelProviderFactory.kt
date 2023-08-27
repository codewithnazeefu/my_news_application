package com.codewithnazeefo.mynewsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codewithnazeefo.mynewsapp.Repository.NewsRepository

@Suppress("UNCHECKED_CAST")
class NewsModelProviderFactory(val app : Application ,val newsRepository:NewsRepository) :  ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return NewsViewModel(app  , newsRepository) as T
    }
}