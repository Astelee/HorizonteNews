package com.horizontenews.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var editSearch: EditText
    private lateinit var layoutSocial: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.recyclerViewSearch)
        editSearch = findViewById(R.id.edit_search)
        layoutSocial = findViewById(R.id.layout_social_bottom)
        
        val btnBack = findViewById<ImageButton>(R.id.btn_back_search)
        val btnDoSearch = findViewById<ImageButton>(R.id.btn_do_search)
        
        // Botões de Redes Sociais
        val btnInsta = findViewById<ImageButton>(R.id.btn_insta)
        val btnWhats = findViewById<ImageButton>(R.id.btn_whatsapp)
        val btnFace = findViewById<ImageButton>(R.id.btn_facebook)

        recyclerView.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener { finish() }

        // Configuração dos Links
        btnInsta.setOnClickListener { abrirLink("https://www.instagram.com/horizontenews_/") }
        btnWhats.setOnClickListener { abrirLink("https://wa.me/5585992823610") }
        btnFace.setOnClickListener { abrirLink("https://www.facebook.com/profile.php?id=61559143715206") }

        btnDoSearch.setOnClickListener {
            val query = editSearch.text.toString()
            if (query.isNotEmpty()) {
                performSearch(query)
            }
        }
    }

    private fun abrirLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun performSearch(query: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/blogger/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BloggerService::class.java)

        service.searchPosts(Config.BLOG_ID, query, Config.API_KEY).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    val posts = response.body()?.items ?: emptyList()
                    if (posts.isNotEmpty()) {
                        layoutSocial.visibility = View.GONE
                    } else {
                        Toast.makeText(this@SearchActivity, "Nenhuma notícia encontrada", Toast.LENGTH_SHORT).show()
                        layoutSocial.visibility = View.VISIBLE
                    }
                    recyclerView.adapter = PostAdapter(posts)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "Erro na busca", Toast.LENGTH_SHORT).show()
            }
        })
    }
}