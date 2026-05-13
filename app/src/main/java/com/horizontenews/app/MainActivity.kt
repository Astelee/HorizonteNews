package com.horizontenews.app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
        // 1. APLICA O TEMA SALVO ANTES DE CARREGAR O LAYOUT
        aplicarTemaSalvo()
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 2. Permissões e Notificações
        verificarPermissaoNotificacao()
        FirebaseMessaging.getInstance().subscribeToTopic("Geral")

        // 3. Configurações da Toolbar e Status Bar
        setupToolbar()

        // 4. Inicialização dos Componentes da Lista
        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 5. CONFIGURAÇÃO DOS BOTÕES (Barra Inferior)
        val btnHome = findViewById<LinearLayout>(R.id.btn_home)
        val btnMenu = findViewById<LinearLayout>(R.id.btn_menu)

        btnHome.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        btnMenu.setOnClickListener {
            // Abre a página de Configurações onde está a engrenagem
            val intent = Intent(this, ThemeActivity::class.java) 
            startActivity(intent)
        }

        // 6. Botão da Lupa (Busca)
        val btnOpenSearch = findViewById<ImageButton>(R.id.btn_open_search)
        btnOpenSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        // 7. Configuração do SwipeRefresh (Laranja Horizonte)
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#F29121"))
        swipeRefreshLayout.setOnRefreshListener { fetchPosts() }

        fetchPosts()
    }

    private fun aplicarTemaSalvo() {
        val sharedPref = getSharedPreferences("theme_pref", Context.MODE_PRIVATE)
        val defaultMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        val savedMode = sharedPref.getInt("app_theme", defaultMode)
        AppCompatDelegate.setDefaultNightMode(savedMode)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        
        // PADRONIZAÇÃO DO TÍTULO: Força o preto igual à tela interna
        val tvLogo = findViewById<TextView>(R.id.tv_logo_toolbar) // Verifique se esse ID existe no seu XML da toolbar
        tvLogo?.let {
            it.setTextColor(Color.BLACK)
            it.typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        
        // Mantém a barra de status no laranja padrão
        window.statusBarColor = Color.parseColor("#F29121")
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
}