package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ConfiguracoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_config)
        toolbar.setNavigationOnClickListener { finish() }

        // Botão de engrenagem no topo
        val btnEngrenagem = findViewById<ImageView>(R.id.btn_engrenagem)
        btnEngrenagem.setOnClickListener {
            startActivity(Intent(this, ThemeActivity::class.java))
        }

        // Configura RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_configuracoes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = listOf(
            ConfigItem(
                title = "Tema",
                subtitle = "Modo claro ou escuro",
                icon = R.drawable.ic_settings_gear,
                action = { startActivity(Intent(this, ThemeActivity::class.java)) }
            ),
            ConfigItem(
                title = "Notificações",
                subtitle = "Gerenciar alertas de notícias",
                icon = android.R.drawable.ic_dialog_info,
                action = { Toast.makeText(this, "Notificações em breve", Toast.LENGTH_SHORT).show() }
            ),
            ConfigItem(
                title = "Tamanho do texto",
                subtitle = "Ajustar tamanho da fonte",
                icon = android.R.drawable.ic_menu_edit,
                action = { Toast.makeText(this, "Tamanho de texto em breve", Toast.LENGTH_SHORT).show() }
            ),
            ConfigItem(
                title = "Limpar cache",
                subtitle = "Liberar espaço",
                icon = android.R.drawable.ic_menu_delete,
                action = { Toast.makeText(this, "Cache limpo!", Toast.LENGTH_SHORT).show() }
            ),
            ConfigItem(
                title = "Sobre o app",
                subtitle = "Versão 1.0 • Horizonte News",
                icon = android.R.drawable.ic_dialog_info,
                action = { Toast.makeText(this, "Horizonte News © 2026", Toast.LENGTH_LONG).show() }
            )
        )

        recyclerView.adapter = ConfiguracoesAdapter(items)
    }
}