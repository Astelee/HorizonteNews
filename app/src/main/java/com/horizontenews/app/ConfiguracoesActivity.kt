package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessaging
import java.io.File

class ConfiguracoesActivity : AppCompatActivity() {

    private var notificacoesAtivadas = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_config)

        // Configura toolbar corretamente
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_configuracoes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = listOf(
            ConfigItem(
                title = "Tema",
                subtitle = "Modo claro ou escuro",
                icon = R.drawable.ic_settings_gear,
                action = {
                    startActivity(Intent(this, ThemeActivity::class.java))
                }
            ),

            ConfigItem(
                title = "Notificações",
                subtitle = "Ativadas / Desativadas",
                icon = android.R.drawable.ic_dialog_info,
                action = { toggleNotificacoes() }
            ),

            ConfigItem(
                title = "Tamanho do texto",
                subtitle = "Ajustar fonte",
                icon = android.R.drawable.ic_menu_edit,
                action = {
                    Toast.makeText(this, "Em breve", Toast.LENGTH_SHORT).show()
                }
            ),

            ConfigItem(
                title = "Limpar cache",
                subtitle = "Liberar espaço",
                icon = android.R.drawable.ic_menu_delete,
                action = { limparCache() }
            ),

            ConfigItem(
                title = "Sobre o app",
                subtitle = "Versão 1.0",
                icon = android.R.drawable.ic_dialog_info,
                action = { mostrarSobre() }
            )
        )

        recyclerView.adapter = ConfiguracoesAdapter(items)
    }

    private fun toggleNotificacoes() {
        notificacoesAtivadas = !notificacoesAtivadas

        if (notificacoesAtivadas) {
            FirebaseMessaging.getInstance().subscribeToTopic("Geral")

            Toast.makeText(
                this,
                "✅ Notificações ATIVADAS",
                Toast.LENGTH_LONG
            ).show()

        } else {

            FirebaseMessaging.getInstance().unsubscribeFromTopic("Geral")

            Toast.makeText(
                this,
                "❌ Notificações DESATIVADAS",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun limparCache() {
        try {
            deleteDir(cacheDir)

            Toast.makeText(
                this,
                "✅ Cache limpo com sucesso!",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "Erro ao limpar cache",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            dir.listFiles()?.forEach {
                deleteDir(it)
            }
        }

        return dir?.delete() ?: false
    }

    private fun mostrarSobre() {
        AlertDialog.Builder(this)
            .setTitle("Horizonte News")
            .setMessage("Versão 1.0\nJornal local de Horizonte - Ceará")
            .setPositiveButton("OK", null)
            .show()
    }
}