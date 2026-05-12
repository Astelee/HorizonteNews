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

        // 1. Pegar os dados enviados pela tela anterior
        val title = intent.getStringExtra("postTitle") ?: ""
        val content = intent.getStringExtra("postContent") ?: ""
        val image = intent.getStringExtra("postImage") ?: ""
        val date = intent.getStringExtra("postDate") ?: ""
        val category = intent.getStringExtra("postCategory") ?: "Notícia"

        // 2. Referenciar os componentes da tela
        val tvTitle = findViewById<TextView>(R.id.postTitleDetail)
        val tvContent = findViewById<TextView>(R.id.postContentDetail)
        val tvDate = findViewById<TextView>(R.id.postDateDetail)
        val tvCategory = findViewById<TextView>(R.id.postCategoryDetail) // Referência da categoria
        val ivImage = findViewById<ImageView>(R.id.postImageDetail)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnShare = findViewById<ImageButton>(R.id.btn_share)

        // 3. Aplicar os textos básicos
        tvTitle.text = title
        tvDate.text = date
        tvCategory.text = category.uppercase() // Define o nome real (ex: VIOLÊNCIA)

        // --- CORREÇÃO DO ERRO "OBJ" E FORMATAÇÃO ---
        // Removemos o caractere \uFFFC (Object Replacement) que gera o erro na imagem 8650.jpg
        val cleanContent = content.replace("\uFFFC", "").replace("￼", "")
        
        // Convertemos o HTML para texto formatado (mantendo negrito, links, etc)
        // DICA: Não use .toString() no final, senão você perde a formatação do HTML!
        tvContent.text = HtmlCompat.fromHtml(cleanContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
        // -------------------------------------------

        // 4. Carregar a imagem da notícia
        Glide.with(this)
            .load(image)
            .placeholder(android.R.color.darker_gray)
            .centerCrop()
            .into(ivImage)

        // 5. Configurar os botões
        btnBack.setOnClickListener { finish() }

        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "$title\n\nLeia mais no Horizonte News")
            startActivity(Intent.createChooser(shareIntent, "Compartilhar notícia"))
        }
    }
}