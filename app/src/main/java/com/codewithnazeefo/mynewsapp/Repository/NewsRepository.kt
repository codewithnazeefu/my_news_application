package com.codewithnazeefo.mynewsapp.Repository

import android.app.DownloadManager.Query
import com.codewithnazeefo.mynewsapp.Model.Article
import com.codewithnazeefo.mynewsapp.api.RetrofitInstance
import com.codewithnazeefo.mynewsapp.db.ArticleDatabase

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)


    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticle()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}