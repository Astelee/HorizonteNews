package com.horizontenews.app

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Configuração da Toolbar para permitir voltar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        // 1. Captura os dados
        val title = intent.getStringExtra("postTitle") ?: ""
        val content = intent.getStringExtra("postContent") ?: ""
        val image = intent.getStringExtra("postImage") ?: ""
        val tempoRelativo = intent.getStringExtra("postDate") ?: ""

        // 2. Referencia as Views
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val tvContent = findViewById<TextView>(R.id.tv_content)
        val tvDate = findViewById<TextView>(R.id.tv_date)
        val ivImage = findViewById<ImageView>(R.id.iv_news_image)

        // 3. Preenchimento e Limpeza de HTML
        tvTitle.text = title
        tvDate.text = "Publicado $tempoRelativo"

        val cleanContent = content.replace(Regex("<img[^>]*>"), "")
            .replace("\uFFFC", "")
            .trim()

        tvContent.text = HtmlCompat.fromHtml(cleanContent, HtmlCompat.FROM_HTML_MODE_LEGACY)

        // 4. Imagem com Glide
        Glide.with(this)
            .load(image)
            .placeholder(android.R.color.darker_gray)
            .centerCrop()
            .into(ivImage)
    }
}