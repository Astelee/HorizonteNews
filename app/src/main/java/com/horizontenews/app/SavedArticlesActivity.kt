package com.horizontenews.app

import android.os.Bundle
import android.widget.Toast
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
            try {
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("postTitle", article.title)
                    putExtra("postContent", article.content)
                    putExtra("postImage", article.imageUrl)
                    putExtra("postDate", article.date)
                    putExtra("postCategory", article.category)
                }
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Erro ao abrir notícia", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerView.adapter = adapter
        
        loadSavedArticles()
    }
    
    private fun loadSavedArticles() {
        lifecycleScope.launch {
            try {
                database.savedArticleDao().getAllSavedArticles().collect { articles ->
                    adapter.updateArticles(articles)
                    if (articles.isEmpty()) {
                        Toast.makeText(this@SavedArticlesActivity, "Nenhuma notícia salva", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@SavedArticlesActivity, "Erro ao carregar salvos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}