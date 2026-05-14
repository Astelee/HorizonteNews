package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnShare = findViewById<ImageButton>(R.id.btn_share)

        // Seta de voltar com animação suave
        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // Compartilhar
        btnShare.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Confira esta matéria no Horizonte News!")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))
        }

        // Configura Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Receber dados da notícia (exemplo)
        val title = intent.getStringExtra("title") ?: "Sem título"
        val content = intent.getStringExtra("content") ?: "Conteúdo não disponível"
        val date = intent.getStringExtra("date") ?: ""
        val imageUrl = intent.getStringExtra("imageUrl")

        findViewById<TextView>(R.id.postTitleDetail).text = title
        findViewById<TextView>(R.id.postContentDetail).text = content
        findViewById<TextView>(R.id.postDateDetail).text = date

        // TODO: Carregar imagem com Glide ou Picasso se quiser
        // Glide.with(this).load(imageUrl).into(findViewById(R.id.postImageDetail))
    }
}