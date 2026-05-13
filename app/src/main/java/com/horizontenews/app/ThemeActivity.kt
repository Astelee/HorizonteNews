package com.horizontenews.app

import android.content.Context
import android.content.Intent
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

        val toolbar = findViewById<Toolbar>(R.id.toolbar_theme)
        
        // Ajusta a cor do ícone de acordo com o tema atual
        val isDarkMode = resources.configuration.uiMode and 
                android.content.res.Configuration.UI_MODE_NIGHT_MASK == 
                android.content.res.Configuration.UI_MODE_NIGHT_YES
        
        val navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow_black)
        if (navigationIcon != null) {
            val wrappedIcon = DrawableCompat.wrap(navigationIcon).mutate()
            // Se estiver no modo escuro, ícone branco. Se não, preto.
            DrawableCompat.setTint(wrappedIcon, if (isDarkMode) Color.WHITE else Color.BLACK)
            toolbar.navigationIcon = wrappedIcon
        }

        toolbar.setNavigationOnClickListener { finish() }

        val radioGroupTheme = findViewById<RadioGroup>(R.id.radioGroupTheme)
        val rbLight = findViewById<RadioButton>(R.id.rbLight)
        val rbDark = findViewById<RadioButton>(R.id.rbDark)
        val rbSystem = findViewById<RadioButton>(R.id.rbSystem)

        when (getSavedTheme()) {
            AppCompatDelegate.MODE_NIGHT_NO -> rbLight.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> rbDark.isChecked = true
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> rbSystem.isChecked = true
        }

        radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                R.id.rbLight -> AppCompatDelegate.MODE_NIGHT_NO
                R.id.rbDark -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }

            if (mode != getSavedTheme()) {
                saveTheme(mode)
                AppCompatDelegate.setDefaultNightMode(mode)
                
                // Reinicia a Activity com uma transição suave para aplicar o tema
                val intent = intent
                finish()
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

    private fun saveTheme(mode: Int) {
        val sharedPref = getSharedPreferences("theme_pref", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
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