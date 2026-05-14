package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: PostAdapter
    private var postList = mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ajustado para os IDs do seu novo XML
        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        
        // Erro anterior: btn_search -> Agora: btn_open_search
        val btnSearch = findViewById<ImageButton>(R.id.btn_open_search)
        
        // Novos botões da Bottom Bar
        val btnHome = findViewById<LinearLayout>(R.id.btn_home)
        val btnMenu = findViewById<LinearLayout>(R.id.btn_menu)

        recyclerView.layoutManager = LinearLayoutManager(this)
        setupAdapter()

        // Configura o carregamento ao puxar
        swipeRefreshLayout.setOnRefreshListener {
            fetchPosts()
        }

        btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        btnMenu.setOnClickListener {
            // Abre a tela de configurações
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }

        btnHome.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        fetchPosts()
    }

    private fun setupAdapter() {
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
        // Mostra o ícone de carregamento do SwipeRefresh
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
                    postList.clear()
                    postList.addAll(posts)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@MainActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
            }
        })
    }
}