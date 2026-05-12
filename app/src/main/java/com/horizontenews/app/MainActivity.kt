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

        setupToolbar()

        // Inicializa os componentes
        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configura o botão da lupa para abrir a tela de pesquisa
        val btnOpenSearch = findViewById<ImageButton>(R.id.btn_open_search)
        btnOpenSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        // Estilo do carregamento
        swipeRefreshLayout.setColorSchemeResources(R.color.orange_news)

        swipeRefreshLayout.setOnRefreshListener {
            fetchPosts()
        }

        fetchPosts()
    }

    private fun fetchPosts() {
        swipeRefreshLayout.isRefreshing = true

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/blogger/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BloggerService::class.java)

        service.getPosts(Config.API_KEY).enqueue(object : Callback<PostResponse> {
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
        window.statusBarColor = getColor(R.color.orange_news)
    }
}