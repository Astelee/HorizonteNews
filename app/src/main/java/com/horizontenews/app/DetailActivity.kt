package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var btnSave: ImageView

    private lateinit var database: AppDatabase

    private var isSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Força modo escuro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        setContentView(R.layout.activity_detail)

        // Banco de dados
        database = AppDatabase.getDatabase(this)

        // Captura os dados vindos do Adapter
        val title = intent.getStringExtra("postTitle") ?: ""
        val content = intent.getStringExtra("postContent") ?: ""
        val image = intent.getStringExtra("postImage") ?: ""
        val tempoRelativo = intent.getStringExtra("postDate") ?: ""
        val category = intent.getStringExtra("postCategory") ?: "Notícia"
        val url = intent.getStringExtra("postUrl") ?: title

        // Mapeia os componentes
        val tvTitle = findViewById<TextView>(R.id.postTitleDetail)
        val tvContent = findViewById<TextView>(R.id.postContentDetail)
        val tvDate = findViewById<TextView>(R.id.postDateDetail)
        val tvCategory = findViewById<TextView>(R.id.postCategoryDetail)
        val ivImage = findViewById<ImageView>(R.id.postImageDetail)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnShare = findViewById<ImageButton>(R.id.btn_share)

        btnSave = findViewById(R.id.btnSave)

        // Permite copiar texto
        tvTitle.setTextIsSelectable(true)
        tvContent.setTextIsSelectable(true)

        // Define os textos
        tvTitle.text = title
        tvCategory.text = category.uppercase()

        tvDate.text = "Publicado $tempoRelativo"

        // Limpeza do HTML
        val htmlWithoutImages = content.replace(Regex("<img[^>]*>"), "")

        val cleanContent = htmlWithoutImages
            .replace("\uFFFC", "")
            .replace("￼", "")
            .trim()

        val formattedContent = HtmlCompat.fromHtml(
            cleanContent,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        tvContent.text = formattedContent.trim()

        // Carrega imagem
        Glide.with(this)
            .load(image)
            .placeholder(android.R.color.darker_gray)
            .centerCrop()
            .into(ivImage)

        // Verifica se já está salva
        lifecycleScope.launch(Dispatchers.IO) {

            isSaved = database
                .savedArticleDao()
                .isArticleSaved(url)

            withContext(Dispatchers.Main) {

                if (isSaved) {

                    btnSave.setImageResource(
                        R.drawable.ic_bookmark_filled
                    )

                } else {

                    btnSave.setImageResource(
                        R.drawable.ic_bookmark_outline
                    )
                }
            }
        }

        // Botão voltar
        btnBack.setOnClickListener {
            finish()
        }

        // Compartilhar notícia
        btnShare.setOnClickListener {

            val shareIntent = Intent(Intent.ACTION_SEND).apply {

                type = "text/plain"

                putExtra(
                    Intent.EXTRA_TEXT,
                    "$title\n\nConfira essa notícia completa no Horizonte News!"
                )
            }

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Compartilhar notícia"
                )
            )
        }

        // Botão salvar
        btnSave.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {

                if (isSaved) {

                    database.savedArticleDao()
                        .unsaveArticle(url)

                    isSaved = false

                    withContext(Dispatchers.Main) {

                        btnSave.setImageResource(
                            R.drawable.ic_bookmark_outline
                        )

                        Toast.makeText(
                            this@DetailActivity,
                            "Removido dos salvos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {

                    val article = SavedArticle(
                        url = url,
                        title = title,
                        category = category,
                        imageUrl = image,
                        date = tempoRelativo,
                        content = cleanContent
                    )

                    database.savedArticleDao()
                        .saveArticle(article)

                    isSaved = true

                    withContext(Dispatchers.Main) {

                        btnSave.setImageResource(
                            R.drawable.ic_bookmark_filled
                        )

                        Toast.makeText(
                            this@DetailActivity,
                            "Notícia salva",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}