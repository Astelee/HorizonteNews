package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ConfiguracoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        // 1. Configura a Toolbar e o botão de voltar
        val toolbar = findViewById<Toolbar>(R.id.toolbar_config)
        toolbar.setNavigationOnClickListener { 
            finish() 
        }

        // 2. Configura o clique na Engrenagem (Canto Superior Direito)
        // Certifique-se que o ID no XML da engrenagem seja 'btn_engrenagem'
        val btnEngrenagem = findViewById<ImageView>(R.id.btn_engrenagem)
        btnEngrenagem.setOnClickListener {
            // Abre a terceira página: ThemeActivity
            val intent = Intent(this, ThemeActivity::class.java)
            startActivity(intent)
        }
    }
}