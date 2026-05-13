package com.horizontenews.app

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

class ThemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)

        // 1. CONFIGURAR A TOOLBAR
        val toolbar = findViewById<Toolbar>(R.id.toolbar_theme)
        
        // CORREÇÃO: Pegamos o ícone e aplicamos o TINT (cor) de forma compatível
        val navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow_black)
        if (navigationIcon != null) {
            val wrappedIcon = DrawableCompat.wrap(navigationIcon).mutate()
            DrawableCompat.setTint(wrappedIcon, Color.BLACK) // Garante a cor preta
            toolbar.navigationIcon = wrappedIcon
        }
        
        // Faz o botão de voltar funcionar
        toolbar.setNavigationOnClickListener {
            finish() 
        }

        // 2. ELEMENTOS DA INTERFACE
        val radioGroupTheme = findViewById<RadioGroup>(R.id.radioGroupTheme)
        val rbLight = findViewById<RadioButton>(R.id.rbLight)
        val rbDark = findViewById<RadioButton>(R.id.rbDark)
        val rbSystem = findViewById<RadioButton>(R.id.rbSystem)

        // Pré-selecionar o botão do tema atual
        when (getSavedTheme()) {
            AppCompatDelegate.MODE_NIGHT_NO -> rbLight.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> rbDark.isChecked = true
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> rbSystem.isChecked = true
        }

        // 3. LÓGICA DO MODO ESCURO
        radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                R.id.rbLight -> AppCompatDelegate.MODE_NIGHT_NO
                R.id.rbDark -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }

            saveTheme(mode)

            // Aplica e recria a tela para mudar a cor na hora
            AppCompatDelegate.setDefaultNightMode(mode)
            delegate.applyDayNight() 
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