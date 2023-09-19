package com.codewithnazeefo.mynewsapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Articles")


data class Article(
    @PrimaryKey(autoGenerate = true)
    val id :Int = 0,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
):Serializable{
    override fun hashCode(): Int {
        var result = id.hashCode()
        if(url.isEmpty()){
            result = 31 * result + url.hashCode()
        }
        return result
    }
}