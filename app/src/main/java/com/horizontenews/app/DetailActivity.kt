package com.astelee.horizontenews  // ← Mude se o seu package for diferente

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.astelee.horizontenews.model.Article  // Ajuste o import conforme seu projeto

class DetailActivity : AppCompatActivity() {

    private lateinit var article: Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Configurar Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detalhes"

        // Receber dados da notícia
        article = intent.getParcelableExtra("article") ?: run {
            finish()
            return
        }

        // Bind das views
        val ivNewsImage: ImageView = findViewById(R.id.iv_news_image)
        val tvTitle: TextView = findViewById(R.id.tv_title)
        val tvDate: TextView = findViewById(R.id.tv_date)
        val tvContent: TextView = findViewById(R.id.tv_content)

        // Preencher dados
        tvTitle.text = article.title
        tvDate.text = article.publishedAt ?: "Data não disponível"
        tvContent.text = article.content ?: article.description ?: "Conteúdo não disponível"

        // Carregar imagem
        article.urlToImage?.let { url ->
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder_news)  // Crie esse drawable se não tiver
                .error(R.drawable.placeholder_news)
                .into(ivNewsImage)
        }
    }

    // Botão voltar da toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}