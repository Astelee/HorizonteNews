package com.horizontenews.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private var postsList: List<Post> = emptyList()
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContentView(R.layout.activity_main)

        database = AppDatabase.getDatabase(this)

        verificarPermissaoNotificacao()
        FirebaseMessaging.getInstance().subscribeToTopic("Geral")

        setupToolbar()

        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Adapter atualizado com callback
        postAdapter = PostAdapter(emptyList(),
            onSaveClick = { post, isSaved, callback ->
                if (isSaved) {
                    savePost(post, callback)
                } else {
                    unsavePost(post, callback)
                }
            },
            getSavedStatus = { post ->
                runBlocking { checkIfSaved(post) }
            }
        )
        recyclerView.adapter = postAdapter

        val btnHome = findViewById<LinearLayout>(R.id.btn_home)
        val btnMenu = findViewById<LinearLayout>(R.id.btn_menu)

        btnHome.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        btnMenu.setOnClickListener {
            val intent = Intent(this, ConfiguracoesActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        val btnOpenSearch = findViewById<ImageButton>(R.id.btn_open_search)
        btnOpenSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down)
        }

        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF6800"))
        swipeRefreshLayout.setOnRefreshListener { fetchPosts() }

        fetchPosts()
    }

    private fun savePost(post: Post, callback: (Boolean) -> Unit) {
        lifecycleScope.launch {
            try {
                val savedArticle = SavedArticle(
                    url = post.url,
                    title = post.title,
                    category = post.firstLabel(),
                    imageUrl = post.firstImage() ?: "",
                    date = post.getTempoRelativo(),
                    content = post.content
                )
                database.savedArticleDao().saveArticle(savedArticle)
                callback(true)
            } catch (e: Exception) {
                callback(false)
            }
        }
    }

    private fun unsavePost(post: Post, callback: (Boolean) -> Unit) {
        lifecycleScope.launch {
            try {
                database.savedArticleDao().unsaveArticle(post.url)
                callback(true)
            } catch (e: Exception) {
                callback(false)
            }
        }
    }

    private suspend fun checkIfSaved(post: Post): Boolean {
        return try {
            database.savedArticleDao().isArticleSaved(post.url)
        } catch (e: Exception) {
            false
        }
    }

    private fun verificarPermissaoNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) 
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, 
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), 
                    101
                )
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
                    postsList = response.body()?.items ?: emptyList()
                    postAdapter.updatePosts(postsList)
                } else {
                    Toast.makeText(this@MainActivity, "Erro ao carregar notícias", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@MainActivity, "Erro de conexão: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        window.statusBarColor = Color.parseColor("#FF6800")
    }

    override fun onResume() {
        super.onResume()
        postAdapter.refreshSavedStatus()
    }
}