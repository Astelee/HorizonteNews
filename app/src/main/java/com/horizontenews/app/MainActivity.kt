package com.horizontenews.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Permissões e Firebase
        verificarPermissaoNotificacao()
        FirebaseMessaging.getInstance().subscribeToTopic("Geral")

        // 2. Configurações Visuais
        setupToolbar()
        
        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 3. Configura a Barra Inferior (Bottom Navigation)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        
        // Garante que o ícone selecionado fique laranja
        bottomNav.itemIconTintList = null 

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Se já estiver no topo, rola para cima
                    recyclerView.smoothScrollToPosition(0)
                    true
                }
                R.id.nav_settings -> {
                    // Abre o menu de Tema (Claro/Escuro)
                    mostrarOpcoesDeTema()
                    true
                }
                else -> false
            }
        }

        // Configura o botão da lupa
        val btnOpenSearch = findViewById<ImageButton>(R.id.btn_open_search)
        btnOpenSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        swipeRefreshLayout.setColorSchemeColors(android.graphics.Color.parseColor("#F29121"))
        swipeRefreshLayout.setOnRefreshListener { fetchPosts() }

        fetchPosts()
    }

    // --- FUNÇÃO PARA TROCAR O TEMA (MODO ESCURO/CLARO) ---
    private fun mostrarOpcoesDeTema() {
        val opcoes = arrayOf("Modo Claro", "Modo Escuro", "Padrão do Sistema")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Configurações de Exibição")
        
        builder.setItems(opcoes) { _, which ->
            when (which) {
                0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
        builder.show()
    }

    private fun verificarPermissaoNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != 
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

    private fun fetchPosts() {
        swipeRefreshLayout.isRefreshing = true
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/blogger/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BloggerService::class.java)
        service.getPosts(Config.BLOG_ID, Config.API_KEY).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val posts = response.body()?.items ?: emptyList()
                    recyclerView.adapter = PostAdapter(posts)
                }
            }
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Barra de status sempre no laranja do Horizonte News
        window.statusBarColor = android.graphics.Color.parseColor("#F29121")
    }
}