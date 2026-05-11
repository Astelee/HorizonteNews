package com.horizontenews.app

// Esta classe representa a resposta inteira da API
data class PostResponse(
    val items: List<Post>
)

// Esta classe representa cada notícia individual
data class Post(
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    // Adicionamos estes dois campos abaixo:
    val published: String,           // Armazena a data de publicação
    val labels: List<String>? = null // Armazena as categorias (tags) do Blogger
)