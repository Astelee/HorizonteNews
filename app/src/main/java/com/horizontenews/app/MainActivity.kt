package com.horizontenews.app

import android.os.Bundle
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

        // Configura as cores do carregamento (Laranja do Horizonte News)
        swipeRefreshLayout.setColorSchemeResources(R.color.orange_news)

        // Configura o que acontece ao puxar para baixo
        swipeRefreshLayout.setOnRefreshListener {
            fetchPosts()
        }

        // Carrega as notícias pela primeira vez ao abrir o app
        fetchPosts()
    }

    private fun fetchPosts() {
        // Mostra a bolinha de carregamento se não estiver aparecendo
        swipeRefreshLayout.isRefreshing = true

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/blogger/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BloggerService::class.java)

        service.getPosts(Config.API_KEY).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                // Para a animação de carregar
                swipeRefreshLayout.isRefreshing = false

                if (response.isSuccessful) {
                    val posts = response.body()?.items ?: emptyList()
                    recyclerView.adapter = PostAdapter(posts)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                // Para a animação de carregar mesmo se der erro
                swipeRefreshLayout.isRefreshing = false
                // TODO: Mostrar mensagem de erro ao usuário (ex: Toast ou Snackbar)
            }
        })
    }

    private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Cor da barra de status (onde fica a bateria/relógio)
        window.statusBarColor = getColor(R.color.orange_news)
    }
}