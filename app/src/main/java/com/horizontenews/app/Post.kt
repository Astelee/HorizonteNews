data class Post(
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    val published: String,
    val labels: List<String>? = null
) {
    // Pega a primeira imagem que estiver dentro do conteúdo HTML
    val firstImage: String
        get() {
            val regex = Regex("<img [^>]*src=\"([^\"]+)\"")
            val match = regex.find(content)
            return match?.groups?.get(1)?.value ?: ""
        }

    // Pega a primeira etiqueta (label) ou retorna "Notícia" se estiver vazio
    val firstLabel: String
        get() = labels?.firstOrNull() ?: "Notícia"
}