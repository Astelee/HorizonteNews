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

        // 1. Captura os dados
        val title = intent.getStringExtra("postTitle") ?: ""
        val content = intent.getStringExtra("postContent") ?: ""
        val image = intent.getStringExtra("postImage") ?: ""
        val date = intent.getStringExtra("postDate") ?: ""
        val category = intent.getStringExtra("postCategory") ?: "Notícia"

        // 2. Mapeia os IDs
        val tvTitle = findViewById<TextView>(R.id.postTitleDetail)
        val tvContent = findViewById<TextView>(R.id.postContentDetail)
        val tvDate = findViewById<TextView>(R.id.postDateDetail)
        val tvCategory = findViewById<TextView>(R.id.postCategoryDetail)
        val ivImage = findViewById<ImageView>(R.id.postImageDetail)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnShare = findViewById<ImageButton>(R.id.btn_share)

        // 3. Define textos
        tvTitle.text = title
        tvDate.text = date
        tvCategory.text = category.uppercase()

        // --- A SOLUÇÃO DEFINITIVA DO QUADRADINHO ---
        // Aqui nós usamos Regex (expressão regular) para procurar e DELETAR 
        // qualquer tag <img> que venha escondida no texto do Blogger.
        val htmlWithoutImages = content.replace(Regex("<img[^>]*>"), "")
        
        // Limpamos o resto da sujeira por precaução
        val cleanContent = htmlWithoutImages
            .replace("\uFFFC", "")
            .replace("￼", "")
            .trim()
        
        // Converte o HTML limpo e aplica o .trim() final para remover espaços sobrando no topo
        val formattedContent = HtmlCompat.fromHtml(cleanContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
        tvContent.text = formattedContent.trim()
        // -------------------------------------------

        // 4. Carrega a Imagem Principal
        Glide.with(this)
            .load(image)
            .placeholder(android.R.color.darker_gray)
            .centerCrop()
            .into(ivImage)

        // 5. Botões
        btnBack.setOnClickListener { finish() }

        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "$title\n\nConfira essa notícia no Horizonte News!")
            }
            startActivity(Intent.createChooser(shareIntent, "Compartilhar notícia"))
        }
    }
}