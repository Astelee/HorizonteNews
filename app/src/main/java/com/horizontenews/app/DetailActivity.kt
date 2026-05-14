package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Captura os dados
        val title = intent.getStringExtra("postTitle") ?: ""
        val content = intent.getStringExtra("postContent") ?: ""
        val image = intent.getStringExtra("postImage") ?: ""
        val tempoRelativo = intent.getStringExtra("postDate") ?: ""
        val category = intent.getStringExtra("postCategory") ?: "Notícia"

        // Views
        val tvTitle = findViewById<TextView>(R.id.postTitleDetail)
        val tvContent = findViewById<TextView>(R.id.postContentDetail)
        val tvDate = findViewById<TextView>(R.id.postDateDetail)
        val tvCategory = findViewById<TextView>(R.id.postCategoryDetail)
        val ivImage = findViewById<ImageView>(R.id.postImageDetail)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnShare = findViewById<ImageButton>(R.id.btn_share)

        // Preenche os dados
        tvTitle.text = title
        tvCategory.text = category.uppercase()
        tvDate.text = "Publicado $tempoRelativo"

        // Limpa HTML
        val cleanContent = content.replace(Regex("<img[^>]*>"), "")
            .replace("\uFFFC", "")
            .replace("￼", "")
            .trim()

        tvContent.text = HtmlCompat.fromHtml(cleanContent, HtmlCompat.FROM_HTML_MODE_LEGACY)

        // Carrega imagem
        Glide.with(this)
            .load(image)
            .placeholder(android.R.color.darker_gray)
            .centerCrop()
            .into(ivImage)

        // Botão voltar
        btnBack.setOnClickListener { finish() }

        // Botão compartilhar
        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "$title\n\nConfira no Horizonte News!")
            }
            startActivity(Intent.createChooser(shareIntent, "Compartilhar"))
        }
    }
}