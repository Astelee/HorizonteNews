package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessaging
import java.io.File

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

        // RecyclerView
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
                action = { abrirNotificacoes() }
            ),
            ConfigItem(
                title = "Tamanho do texto",
                subtitle = "Ajustar tamanho da fonte",
                icon = android.R.drawable.ic_menu_edit,
                action = { ajustarTamanhoTexto() }
            ),
            ConfigItem(
                title = "Limpar cache",
                subtitle = "Liberar espaço armazenado",
                icon = android.R.drawable.ic_menu_delete,
                action = { limparCache() }
            ),
            ConfigItem(
                title = "Sobre o app",
                subtitle = "Versão 1.0 • Horizonte News",
                icon = android.R.drawable.ic_dialog_info,
                action = { mostrarSobre() }
            )
        )

        recyclerView.adapter = ConfiguracoesAdapter(items)
    }

    private fun abrirNotificacoes() {
        FirebaseMessaging.getInstance().subscribeToTopic("Geral")
        Toast.makeText(this, "✅ Notificações ativadas!", Toast.LENGTH_LONG).show()
    }

    private fun ajustarTamanhoTexto() {
        val options = arrayOf("Pequeno", "Médio (Padrão)", "Grande")
        AlertDialog.Builder(this)
            .setTitle("Tamanho do texto")
            .setItems(options) { _, which ->
                val scale = when (which) {
                    0 -> 0.85f
                    1 -> 1.0f
                    2 -> 1.18f
                    else -> 1.0f
                }
                getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
                    .putFloat("font_scale", scale).apply()
                
                Toast.makeText(this, "Tamanho alterado! Reinicie o app.", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun limparCache() {
        try {
            deleteDir(cacheDir)
            Toast.makeText(this, "✅ Cache limpo com sucesso!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao limpar cache", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            dir.listFiles()?.forEach { child ->
                deleteDir(child)
            }
        }
        return dir?.delete() ?: false
    }

    private fun mostrarSobre() {
        AlertDialog.Builder(this)
            .setTitle("Sobre Horizonte News")
            .setMessage("Versão 1.0\n\nJornal local de Horizonte - Ceará\n\nDesenvolvido para a comunidade.")
            .setPositiveButton("OK", null)
            .show()
    }
}