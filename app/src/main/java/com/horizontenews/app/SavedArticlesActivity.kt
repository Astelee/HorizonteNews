package com.horizontenews.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class SavedArticlesActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedArticlesAdapter
    private lateinit var database: AppDatabase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_articles)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Notícias Salvas"
        
        database = AppDatabase.getDatabase(this)
        
        recyclerView = findViewById(R.id.recyclerViewSaved)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = SavedArticlesAdapter(emptyList()) { article ->
            // Abrir detalhe da notícia salva
            val intent = android.content.Intent(this, DetailActivity::class.java).apply {
                putExtra("postTitle", article.title)
                putExtra("postContent", article.content)
                putExtra("postImage", article.imageUrl)
                putExtra("postDate", article.date)
                putExtra("postCategory", article.category)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        
        loadSavedArticles()
    }
    
    private fun loadSavedArticles() {
        lifecycleScope.launch {
            database.savedArticleDao().getAllSavedArticles().collect { articles ->
                adapter.updateArticles(articles)
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}