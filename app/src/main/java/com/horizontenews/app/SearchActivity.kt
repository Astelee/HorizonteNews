package com.horizontenews.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
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

        // Referências dos componentes
        val editSearch = findViewById<EditText>(R.id.edit_search)
        val btnBack = findViewById<ImageButton>(R.id.btn_back_search)
        val btnInsta = findViewById<ImageButton>(R.id.btn_insta)
        val btnWhatsapp = findViewById<ImageButton>(R.id.btn_whatsapp)
        val btnFacebook = findViewById<ImageButton>(R.id.btn_facebook)
        val layoutSocial = findViewById<LinearLayout>(R.id.layout_social_bottom)
        val recyclerResults = findViewById<RecyclerView>(R.id.recycler_search_results)

        // Configuração do RecyclerView
        recyclerResults.layoutManager = LinearLayoutManager(this)

        // Botão Voltar (Agora com o preto real que configuramos)
        btnBack.setOnClickListener { finish() }

        // Ações das Redes Sociais com seus links oficiais
        btnInsta.setOnClickListener {
            abrirLink("https://www.instagram.com/horizontenews?igsh=MTJwdnd6bmE5amdseg==")
        }

        btnWhatsapp.setOnClickListener {
            abrirLink("https://wa.me/5585994130806")
        }

        btnFacebook.setOnClickListener {
            abrirLink("https://www.facebook.com/HorizonteNews")
        }

        // Lógica da pesquisa ao clicar na Lupa do teclado ou no ícone
        editSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                executarBusca(editSearch, layoutSocial, recyclerResults)
                true
            } else false
        }
    }

    private fun executarBusca(editText: EditText, layoutSocial: View, recyclerView: RecyclerView) {
        val query = editText.text.toString()
        if (query.isNotEmpty()) {
            // Esconde o rodapé social para dar espaço aos resultados
            layoutSocial.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            realizarBuscaApi(query, recyclerView)
        }
    }

    private fun abrirLink(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            // Caso o navegador falhe, o app não trava
        }
    }

    private fun realizarBuscaApi(query: String, recyclerView: RecyclerView) {
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
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                // Erro de conexão
            }
        })
    }
}