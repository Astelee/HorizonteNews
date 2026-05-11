package com.horizontenews.app

data class PostResponse(val items: List<Post>)
data class Post(
    val id: String,
    val title: String,
    val content: String,
    val url: String
)