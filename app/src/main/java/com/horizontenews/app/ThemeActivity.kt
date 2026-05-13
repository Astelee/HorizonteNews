package com.horizontenews.app

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

class ThemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)

        // 1. CONFIGURAR A TOOLBAR COM O SEU ÍCONE PRETO
        val toolbar = findViewById<Toolbar>(R.id.toolbar_theme)
        
        // Usando o seu ícone que já existe na pasta drawable
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_back_black)
        
        // Garante que o ícone fique PRETO e não cinza (bug do sistema)
        toolbar.setNavigationIconTint(android.graphics.Color.BLACK)
        
        // Faz o botão de voltar funcionar de verdade
        toolbar.setNavigationOnClickListener {
            finish() // Fecha a tela e volta para a anterior
        }

        // 2. ELEMENTOS DA INTERFACE
        val radioGroupTheme = findViewById<RadioGroup>(R.id.radioGroupTheme)
        val rbLight = findViewById<RadioButton>(R.id.rbLight)
        val rbDark = findViewById<RadioButton>(R.id.rbDark)
        val rbSystem = findViewById<RadioButton>(R.id.rbSystem)

        // Carregar a preferência salva para marcar o botão certo ao abrir
        when (getSavedTheme()) {
            AppCompatDelegate.MODE_NIGHT_NO -> rbLight.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> rbDark.isChecked = true
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> rbSystem.isChecked = true
        }

        // 3. LÓGICA DE MUDANÇA DE TEMA (MODO ESCURO)
        radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                R.id.rbLight -> AppCompatDelegate.MODE_NIGHT_NO
                R.id.rbDark -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }

            // Salva a escolha para o app não esquecer ao fechar
            saveTheme(mode)

            // APLICA NA HORA
            AppCompatDelegate.setDefaultNightMode(mode)
            delegate.applyDayNight() 
            
            // Recria a tela para as cores novas (Preto/Branco) carregarem
            recreate() 
        }
    }

    private fun saveTheme(mode: Int) {
        val sharedPref = getSharedPreferences("theme_pref", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt("app_theme", mode)
            apply()
        }
    }

    private fun getSavedTheme(): Int {
        val sharedPref = getSharedPreferences("theme_pref", Context.MODE_PRIVATE)
        val defaultMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        return sharedPref.getInt("app_theme", defaultMode)
    }
}