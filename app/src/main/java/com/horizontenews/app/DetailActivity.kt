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

        // 1. Captura os dados enviados pelo Intent
        val title = intent.getStringExtra("postTitle") ?: ""
        val content = intent.getStringExtra("postContent") ?: ""
        val image = intent.getStringExtra("postImage") ?: ""
        val date = intent.getStringExtra("postDate") ?: ""
        val category = intent.getStringExtra("postCategory") ?: "Notícia"

        // 2. Referencia os componentes do XML
        val tvTitle = findViewById<TextView>(R.id.postTitleDetail)
        val tvContent = findViewById<TextView>(R.id.postContentDetail)
        val tvDate = findViewById<TextView>(R.id.postDateDetail)
        val tvCategory = findViewById<TextView>(R.id.postCategoryDetail)
        val ivImage = findViewById<ImageView>(R.id.postImageDetail)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnShare = findViewById<ImageButton>(R.id.btn_share)

        // 3. Aplica os textos e formatação da categoria
        tvTitle.text = title
        tvDate.text = date
        tvCategory.text = category.uppercase()

        // --- CORREÇÃO DEFINITIVA DO ERRO "OBJ" ---
        // 1. Removemos o caractere especial Unicode \uFFFC
        // 2. Removemos o caractere de substituição visual ￼
        // 3. Aplicamos o .trim() para remover espaços vazios que sobram no início/fim
        val cleanContent = content
            .replace("\uFFFC", "")
            .replace("￼", "")
            .trim()
        
        // Convertemos o HTML para texto formatado (Spanned)
        val formattedContent = HtmlCompat.fromHtml(cleanContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
        tvContent.text = formattedContent
        // -----------------------------------------

        // 4. Carrega a imagem da notícia com Glide
        Glide.with(this)
            .load(image)
            .placeholder(android.R.color.darker_gray)
            .centerCrop()
            .into(ivImage)

        // 5. Configuração das ações dos botões
        btnBack.setOnClickListener { 
            finish() 
        }

        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val message = "$title\n\nConfira essa e outras notícias no Horizonte News!"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(shareIntent, "Compartilhar notícia"))
        }
    }
}