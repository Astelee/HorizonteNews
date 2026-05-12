package com.horizontenews.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val editSearch = findViewById<EditText>(R.id.edit_search)
        val btnBack = findViewById<ImageButton>(R.id.btn_back_search)
        val btnInsta = findViewById<ImageButton>(R.id.btn_insta)
        val btnWhatsapp = findViewById<ImageButton>(R.id.btn_whatsapp)
        val btnFacebook = findViewById<ImageButton>(R.id.btn_facebook)
        val layoutSocial = findViewById<LinearLayout>(R.id.layout_social_bottom)
        val recyclerResults = findViewById<RecyclerView>(R.id.recycler_search_results)

        recyclerResults.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener { finish() }

        // Link Instagram Oficial
        btnInsta.setOnClickListener {
            abrirLink("https://www.instagram.com/horizontenews?igsh=MTJwdnd6bmE5amdseg==")
        }

        // Link WhatsApp Oficial
        btnWhatsapp.setOnClickListener {
            abrirLink("https://wa.me/5585994130806")
        }

        // Link Facebook Oficial
        btnFacebook.setOnClickListener {
            abrirLink("https://www.facebook.com/HorizonteNews")
        }

        // Lógica da pesquisa
        editSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = editSearch.text.toString()
                if (query.isNotEmpty()) {
                    layoutSocial.visibility = View.GONE
                    recyclerResults.visibility = View.VISIBLE
                    realizarBusca(query, recyclerResults)
                }
                true
            } else false
        }
    }

    private fun abrirLink(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            // Caso o usuário não tenha o app instalado ou o link falhe
        }
    }

    private fun realizarBusca(query: String, recyclerView: RecyclerView) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/blogger/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BloggerService::class.java)
        
        service.searchPosts(Config.BLOG_ID, Config.API_KEY, query).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    val posts = response.body()?.items ?: emptyList()
                    recyclerView.adapter = PostAdapter(posts)
                }
            }
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {}
        })
    }
}