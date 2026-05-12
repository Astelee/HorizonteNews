package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

        // Configura a barra superior (Toolbar)
        setupToolbar()

        // Inicializa os componentes da tela principal
        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configura o botão da lupa para abrir a tela de pesquisa
        // Agora usando a lupa preta (ic_search_black) definida no XML
        val btnOpenSearch = findViewById<ImageButton>(R.id.btn_open_search)
        btnOpenSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        // Estilo do carregamento (Laranja do Horizonte News)
        // Usamos o código hexadecimal direto para evitar erros caso o colors.xml falhe
        swipeRefreshLayout.setColorSchemeColors(android.graphics.Color.parseColor("#F29121"))

        swipeRefreshLayout.setOnRefreshListener {
            fetchPosts()
        }

        // Carrega as notícias ao abrir o app
        fetchPosts()
    }

    private fun fetchPosts() {
        // Inicia a animação de carregamento (o círculo girando)
        swipeRefreshLayout.isRefreshing = true

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/blogger/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BloggerService::class.java)

        // Busca as notícias no Blogger usando as chaves de configuração
        service.getPosts(Config.BLOG_ID, Config.API_KEY).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val posts = response.body()?.items ?: emptyList()
                    recyclerView.adapter = PostAdapter(posts)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                // Para a animação mesmo em caso de erro de conexão ou internet
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        
        // Remove o título padrão para usar o "HORIZONTE NEWS" personalizado do seu XML
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Define a cor da barra de status (onde fica o relógio e bateria) como Laranja
        window.statusBarColor = android.graphics.Color.parseColor("#F29121")
    }
}