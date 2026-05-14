package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 1. Captura os dados enviados pela MainActivity
        val title = intent.getStringExtra("postTitle") ?: ""
        val content = intent.getStringExtra("postContent") ?: ""
        val image = intent.getStringExtra("postImage") ?: ""
        val tempoRelativo = intent.getStringExtra("postDate") ?: ""

        // 2. Referencia as Views usando os IDs que estão no seu arquivo XML
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val tvContent = findViewById<TextView>(R.id.tv_content)
        val tvDate = findViewById<TextView>(R.id.tv_date)
        val ivImage = findViewById<ImageView>(R.id.iv_news_image)

        // 3. Preenche os campos com as informações
        tvTitle.text = title
        tvDate.text = "Publicado $tempoRelativo"

        // Limpa códigos HTML do texto da notícia
        val cleanContent = content.replace(Regex("<img[^>]*>"), "")
            .replace("\uFFFC", "")
            .replace("￼", "")
            .trim()

        tvContent.text = HtmlCompat.fromHtml(cleanContent, HtmlCompat.FROM_HTML_MODE_LEGACY)

        // 4. Carrega a imagem usando a biblioteca Glide
        Glide.with(this)
            .load(image)
            .placeholder(android.R.color.darker_gray)
            .centerCrop()
            .into(ivImage)
    }
}
