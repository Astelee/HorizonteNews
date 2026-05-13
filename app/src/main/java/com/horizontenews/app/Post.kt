package com.horizontenews.app

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Pattern

// Esta é a classe que o Retrofit usa para entender a resposta do Blogger
// O erro "Unresolved reference: PostResponse" acontece porque ela estava faltando
data class PostResponse(
    val items: List<Post>? = null
)

data class Post(
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    val published: String,           
    val labels: List<String>? = null 
) {
    // Gera o "Há X tempo" (Ex: há 2 horas)
    fun getTempoRelativo(): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            val dataPublicacao = sdf.parse(published)?.time ?: 0L
            val agora = System.currentTimeMillis()

            val rts = DateUtils.getRelativeTimeSpanString(
                dataPublicacao, 
                agora, 
                DateUtils.MINUTE_IN_MILLIS
            )

            rts.toString()
        } catch (e: Exception) {
            "Recente"
        }
    }

    // Extrai a primeira imagem do conteúdo HTML da notícia
    fun firstImage(): String? {
        val matcher = Pattern.compile("<img.*src=\"([^\"]+)\"").matcher(content)
        return if (matcher.find()) matcher.group(1) else null
    }

    // Pega a primeira etiqueta (categoria) da notícia
    fun firstLabel(): String {
        return labels?.firstOrNull() ?: "Notícias"
    }
}