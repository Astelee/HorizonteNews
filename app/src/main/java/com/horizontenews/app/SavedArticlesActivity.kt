package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
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

        adapter = SavedArticlesAdapter(
            emptyList()
        ) { article ->

            try {

                val intent = Intent(
                    this,
                    DetailActivity::class.java
                ).apply {

                    putExtra(
                        "postTitle",
                        article.title
                    )

                    putExtra(
                        "postContent",
                        article.content
                    )

                    putExtra(
                        "postImage",
                        article.imageUrl
                    )

                    putExtra(
                        "postDate",
                        article.date
                    )

                    putExtra(
                        "postCategory",
                        article.category
                    )

                    putExtra(
                        "postUrl",
                        article.url
                    )
                }

                startActivity(intent)

            } catch (e: Exception) {

                Toast.makeText(
                    this,
                    "Erro ao abrir notícia",
                    Toast.LENGTH_SHORT
                ).show()

                e.printStackTrace()
            }
        }

        recyclerView.adapter = adapter

        loadSavedArticles()
    }

    private fun loadSavedArticles() {

        lifecycleScope.launch {

            try {

                database
                    .savedArticleDao()
                    .getAllSavedArticles()
                    .collectLatest { savedList ->

                        adapter.updateArticles(savedList)
                    }

            } catch (e: Exception) {

                Toast.makeText(
                    this@SavedArticlesActivity,
                    "Erro ao carregar notícias",
                    Toast.LENGTH_LONG
                ).show()

                e.printStackTrace()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        finish()

        return true
    }
}