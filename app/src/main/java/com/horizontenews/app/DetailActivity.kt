package com.horizontenews.app

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Pegando os dados enviados pela MainActivity
        val title = intent.getStringExtra("postTitle") ?: ""
        val content = intent.getStringExtra("postContent") ?: ""
        val image = intent.getStringExtra("postImage") ?: ""
        val date = intent.getStringExtra("postDate") ?: ""

        // Vinculando os componentes do XML
        val tvTitle = findViewById<TextView>(R.id.postTitleDetail)
        val tvContent = findViewById<TextView>(R.id.postContentDetail)
        val tvDate = findViewById<TextView>(R.id.postDateDetail)
        val ivImage = findViewById<ImageView>(R.id.postImageDetail)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnShare = findViewById<ImageButton>(R.id.btn_share)

        // Preenchendo os dados
        tvTitle.text = title
        tvContent.text = content
        tvDate.text = date
        
        Glide.with(this)
            .load(image)
            .placeholder(android.R.color.darker_gray)
            .into(ivImage)

        // Configurando o botão de voltar
        btnBack.setOnClickListener {
            finish()
        }

        // Configurando o botão de compartilhar
        btnShare.setOnClickListener {
            val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "$title\n\nLeia mais no Horizonte News")
            startActivity(android.content.Intent.createChooser(shareIntent, "Compartilhar notícia"))
        }
    }
}