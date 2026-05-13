package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 1. Captura os dados vindos do Adapter
        val title = intent.getStringExtra("postTitle") ?: ""
        val content = intent.getStringExtra("postContent") ?: ""
        val image = intent.getStringExtra("postImage") ?: ""
        val rawDate = intent.getStringExtra("postDate") ?: "" 
        val category = intent.getStringExtra("postCategory") ?: "Notícia"

        // 2. Mapeia os componentes do layout
        val tvTitle = findViewById<TextView>(R.id.postTitleDetail)
        val tvContent = findViewById<TextView>(R.id.postContentDetail)
        val tvDate = findViewById<TextView>(R.id.postDateDetail)
        val tvCategory = findViewById<TextView>(R.id.postCategoryDetail)
        val ivImage = findViewById<ImageView>(R.id.postImageDetail)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnShare = findViewById<ImageButton>(R.id.btn_share)

        // --- ADIÇÃO PARA PERMITIR COPIAR TEXTO ---
        tvTitle.setTextIsSelectable(true)
        tvContent.setTextIsSelectable(true)
        // -----------------------------------------

        // 3. Lógica de Formatação da Data
        val formattedDate = try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val formatter = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.getDefault())
            val date = parser.parse(rawDate)
            date?.let { formatter.format(it) } ?: rawDate
        } catch (e: Exception) {
            rawDate
        }

        // 4. Define os textos básicos
        tvTitle.text = title
        tvDate.text = "Publicado em: $formattedDate"
        tvCategory.text = category.uppercase()

        // 5. Limpeza do HTML
        val htmlWithoutImages = content.replace(Regex("<img[^>]*>"), "")
        val cleanContent = htmlWithoutImages
            .replace("\uFFFC", "")
            .replace("￼", "")
            .trim()

        val formattedContent = HtmlCompat.fromHtml(cleanContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
        tvContent.text = formattedContent.trim()

        // 6. Carrega a Imagem Principal com Glide
        Glide.with(this)
            .load(image)
            .placeholder(android.R.color.darker_gray)
            .centerCrop()
            .into(ivImage)

        // 7. Configurações dos Botões
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