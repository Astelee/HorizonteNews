package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        val btnBack =
            findViewById<ImageButton>(R.id.btn_back)

        val btnShare =
            findViewById<ImageButton>(R.id.btn_share)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnShare.setOnClickListener {

            val shareIntent = Intent().apply {

                action = Intent.ACTION_SEND

                putExtra(
                    Intent.EXTRA_TEXT,
                    "Confira esta matéria no Horizonte News!"
                )

                type = "text/plain"
            }

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Compartilhar via"
                )
            )
        }

        val title =
            intent.getStringExtra("title")
                ?: "Sem título"

        val content =
            intent.getStringExtra("content")
                ?: "Conteúdo não disponível"

        val date =
            intent.getStringExtra("date")
                ?: ""

        findViewById<TextView>(
            R.id.postTitleDetail
        ).text = title

        findViewById<TextView>(
            R.id.postContentDetail
        ).text = content

        findViewById<TextView>(
            R.id.postDateDetail
        ).text = date
    }
}