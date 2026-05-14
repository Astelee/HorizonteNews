package com.horizontenews.app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_articles")
data class SavedArticle(

    @PrimaryKey
    val url: String,

    val title: String,
    val category: String,
    val imageUrl: String,
    val date: String,
    val content: String,

    val savedAt: Long = System.currentTimeMillis()
)