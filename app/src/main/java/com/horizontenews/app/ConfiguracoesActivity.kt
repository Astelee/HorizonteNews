package com.horizontenews.app

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class ConfiguracoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        // Configura o clique na linha do Modo Escuro
        val btnModoEscuro = findViewById<LinearLayout>(R.id.btn_modo_escuro)
        btnModoEscuro.setOnClickListener {
            mostrarOpcoesDeTema()
        }
        
        // Botão voltar da Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_config)
        toolbar.setNavigationOnClickListener { 
            finish() 
        }
    }

    private fun mostrarOpcoesDeTema() {
        val opcoes = arrayOf("Sempre claro", "Sempre escuro", "Automático")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecione o modo")

        builder.setItems(opcoes) { _, which ->
            when (which) {
                0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            delegate.applyDayNight()
        }
        builder.show()
    }
}