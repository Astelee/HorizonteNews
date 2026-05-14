package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Força modo escuro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        
        setContentView(R.layout.activity_detail)

        // 1. Captura os dados vindos do Adapter (já formatados pelo PostAdapter)
        val title = intent.getStringExtra("postTitle") ?: ""
        val content = intent.getStringExtra("postContent") ?: ""
        val image = intent.getStringExtra("postImage") ?: ""
        val tempoRelativo = intent.getStringExtra("postDate") ?: "" 
        val category = intent.getStringExtra("postCategory") ?: "Notícia"

        // 2. Mapeia os componentes do layout
        val tvTitle = findViewById<TextView>(R.id.postTitleDetail)
        val tvContent = findViewById<TextView>(R.id.postContentDetail)
        val tvDate = findViewById<TextView>(R.id.postDateDetail)
        val tvCategory = findViewById<TextView>(R.id.postCategoryDetail)
        val ivImage = findViewById<ImageView>(R.id.postImageDetail)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnShare = findViewById<ImageButton>(R.id.btn_share)

        // Permite copiar texto
        tvTitle.setTextIsSelectable(true)
        tvContent.setTextIsSelectable(true)

        // 3. Define os textos
        tvTitle.text = title
        tvCategory.text = category.uppercase()

        // CORREÇÃO: Agora exibe apenas "Publicado há 8 horas" sem o "em:"
        tvDate.text = "Publicado $tempoRelativo"

        // 4. Limpeza do HTML
        val htmlWithoutImages = content.replace(Regex("<img[^>]*>"), "")
        val cleanContent = htmlWithoutImages
            .replace("\uFFFC", "")
            .replace("￼", "")
            .trim()

        val formattedContent = HtmlCompat.fromHtml(cleanContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
        tvContent.text = formattedContent.trim()

        // 5. Carrega a Imagem Principal
        Glide.with(this)
            .load(image)
            .placeholder(android.R.color.darker_gray)
            .centerCrop()
            .into(ivImage)

        // 6. Configurações dos Botões
        btnBack.setOnClickListener { finish() }

        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "$title\n\nConfira essa notícia completa no Horizonte News!")
            }
            startActivity(Intent.createChooser(shareIntent, "Compartilhar notícia"))
        }
    }
}