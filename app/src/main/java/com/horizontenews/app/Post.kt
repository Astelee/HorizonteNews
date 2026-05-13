package com.horizontenews.app

// Esta classe representa a resposta inteira da API do Blogger
data class PostResponse(
    val items: List<Post>
)

// Esta classe representa cada notícia individual
data class Post(
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    val published: String,           
    val labels: List<String>? = null 
) {
    // Extrai a primeira imagem do conteúdo HTML para o seu layout de portal
    val firstImage: String
        get() {
            val regex = Regex("<img [^>]*src=\"([^\"]+)\"")
            val match = regex.find(content)
            return match?.groups?.get(1)?.value ?: ""
        }

    // Pega a primeira etiqueta (categoria) ou define "Geral"
    val firstLabel: String
        get() = labels?.firstOrNull() ?: "Geral"
}