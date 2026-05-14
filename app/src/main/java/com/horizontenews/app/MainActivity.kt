package com.horizontenews.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Aplica o tema salvo antes do setContentView
        aplicarTemaSalvo()
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        
        val btnSearch = findViewById<ImageButton>(R.id.btn_search)
        val btnSettings = findViewById<ImageButton>(R.id.btn_settings)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Navegação para busca e configurações
        btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }

        // Carrega as notícias iniciais
        fetchPosts()
    }

    private fun aplicarTemaSalvo() {
        val sharedPref = getSharedPreferences("theme_pref", Context.MODE_PRIVATE)
        val mode = sharedPref.getInt("app_theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun fetchPosts() {
        progressBar.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/blogger/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BloggerService::class.java)

        service.getPosts(Config.BLOG_ID, Config.API_KEY).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val posts = response.body()?.items ?: emptyList()
                    recyclerView.adapter = PostAdapter(posts) { post ->
                        val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                            putExtra("postTitle", post.title)
                            putExtra("postContent", post.content)
                            putExtra("postImage", post.firstImage())
                            putExtra("postDate", post.getTempoRelativo())
                        }
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Erro ao carregar notícias", Toast.LENGTH_SHORT).show()
            }
        })
    }
}