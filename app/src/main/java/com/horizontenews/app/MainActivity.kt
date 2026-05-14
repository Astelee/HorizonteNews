package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var adapter: PostAdapter
    private var postList = mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuração da UI
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnSearch = findViewById<ImageButton>(R.id.btn_search)
        val btnSettings = findViewById<ImageButton>(R.id.btn_settings)

        // Botão de Busca
        btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        // Botão de Configurações
        btnSettings.setOnClickListener {
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }

        setupRecyclerView()
        fetchPosts()
    }

    private fun setupRecyclerView() {
        // Agora o adaptador recebe apenas a lista e a ação de clique
        adapter = PostAdapter(postList) { post ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("postTitle", post.title)
                putExtra("postContent", post.content)
                putExtra("postImage", post.firstImage())
                putExtra("postDate", post.getTempoRelativo())
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
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
                    postList.clear()
                    postList.addAll(posts)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@MainActivity, "Erro ao carregar notícias", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Falha na conexão", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Nota: A função refreshSavedStatus foi removida pois não usamos mais persistência local
}