package com.codewithnazeefo.mynewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codewithnazeefo.mynewsapp.Model.Article


@Dao
interface articleDao {
       @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long

    @Query("SELECT * FROM articles")
    fun getAllArticle():LiveData<List<Article>>


    @Delete
    suspend fun deleteArticle(article: Article)


}