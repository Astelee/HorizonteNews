package com.horizontenews.app

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar

class ThemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)

        // Configurar a Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Tema do App"

        // Encontrar o RadioGroup e os RadioButtons
        val radioGroupTheme = findViewById<RadioGroup>(R.id.radioGroupTheme)
        val rbLight = findViewById<RadioButton>(R.id.rbLight)
        val rbDark = findViewById<RadioButton>(R.id.rbDark)
        val rbSystem = findViewById<RadioButton>(R.id.rbSystem)

        // Carregar o tema atual para pré-selecionar o botão correto
        when (getSavedTheme()) {
            AppCompatDelegate.MODE_NIGHT_NO -> rbLight.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> rbDark.isChecked = true
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> rbSystem.isChecked = true
        }

        // Configurar o listener para quando o usuário clicar em uma opção
        radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                R.id.rbLight -> AppCompatDelegate.MODE_NIGHT_NO
                R.id.rbDark -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }

            // --- AQUI ESTÁ A SOLUÇÃO DO BUG ---
            // 1. Salvar a preferência (para o app lembrar ao abrir de novo)
            saveTheme(mode)

            // 2. Aplicar o modo imediatamente (sem pop-up)
            AppCompatDelegate.setDefaultNightMode(mode)
            delegate.applyDayNight() // Força a atualização da view

            // 3. Recriar a atividade para garantir que tudo mude de cor
            recreate() 
        }
    }

    // Funções para salvar e carregar a preferência de tema (usando SharedPreferences)
    private fun saveTheme(mode: Int) {
        val sharedPref = getSharedPreferences("theme_pref", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("app_theme", mode)
            apply()
        }
    }

    private fun getSavedTheme(): Int {
        val sharedPref = getSharedPreferences("theme_pref", Context.MODE_PRIVATE)
        // O padrão é o sistema, ou Modo Claro para Androids antigos
        val defaultMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        return sharedPref.getInt("app_theme", defaultMode)
    }

    // Botão Voltar da Toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}