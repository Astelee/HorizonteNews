package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ConfiguracoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        // Configurações disponíveis (sem Meus Salvos)
        val btnTema = findViewById<LinearLayout>(R.id.btn_tema)
        val btnCompartilhar = findViewById<LinearLayout>(R.id.btn_compartilhar)
        val btnSobre = findViewById<LinearLayout>(R.id.btn_sobre)

        btnTema?.setOnClickListener {
            // TODO: Implementar troca de tema
        }

        btnCompartilhar?.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Baixe o Horizonte News: https://play.google.com/store/apps/details?id=com.horizontenews.app")
            }
            startActivity(Intent.createChooser(shareIntent, "Compartilhar app"))
        }

        btnSobre?.setOnClickListener {
            // TODO: Abrir tela sobre
        }
    }
}