package com.horizontenews.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
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
    private lateinit var btnClear: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Inicialização dos componentes conforme o XML
        recyclerView = findViewById(R.id.recyclerViewSearch)
        editSearch = findViewById(R.id.edit_search)
        layoutSocial = findViewById(R.id.layout_social_bottom)
        btnClear = findViewById(R.id.btn_clear_search)
        progressBar = findViewById(R.id.progress_search)
        tvEmpty = findViewById(R.id.tv_empty_search)
        
        val btnBack = findViewById<ImageButton>(R.id.btn_back_search)
        val btnInsta = findViewById<ImageButton>(R.id.btn_insta)
        val btnWhats = findViewById<ImageButton>(R.id.btn_whatsapp)
        val btnFace = findViewById<ImageButton>(R.id.btn_facebook)

        recyclerView.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener { finish() }

        // Lógica do botão "X" (Limpar)
        btnClear.setOnClickListener {
            editSearch.text.clear()
            tvEmpty.visibility = View.GONE
            // Opcional: mostrar novamente as redes sociais ao limpar
            layoutSocial.visibility = View.VISIBLE
        }

        // Monitora o texto para mostrar/esconder o "X" em tempo real
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    btnClear.visibility = View.GONE
                } else if (progressBar.visibility != View.VISIBLE) {
                    btnClear.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Configuração dos links das redes sociais
        btnInsta.setOnClickListener { abrirLink("https://www.instagram.com/horizontenews_/") }
        btnWhats.setOnClickListener { abrirLink("https://wa.me/5585994130806") }
        btnFace.setOnClickListener { abrirLink("https://www.facebook.com/share/1AJNBnodHo/") }

        // Dispara a busca ao clicar na lupa do teclado
        editSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = editSearch.text.toString().trim()
                if (query.isNotEmpty()) {
                    performSearch(query)
                }
                true
            } else {
                false
            }
        }
    }

    private fun performSearch(query: String) {
        // Estado visual de "Carregando"
        progressBar.visibility = View.VISIBLE
        btnClear.visibility = View.GONE
        tvEmpty.visibility = View.GONE
        layoutSocial.visibility = View.GONE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/blogger/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BloggerService::class.java)

        service.searchPosts(Config.BLOG_ID, query, Config.API_KEY).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                progressBar.visibility = View.GONE
                
                // Só volta a mostrar o X se o campo não foi limpo durante a busca
                if (editSearch.text.isNotEmpty()) {
                    btnClear.visibility = View.VISIBLE
                }

                if (response.isSuccessful) {
                    val posts = response.body()?.items ?: emptyList()
                    
                    if (posts.isNotEmpty()) {
                        tvEmpty.visibility = View.GONE
                        recyclerView.adapter = PostAdapter(posts)
                    } else {
                        tvEmpty.visibility = View.VISIBLE
                        recyclerView.adapter = PostAdapter(emptyList())
                    }
                } else {
                    Toast.makeText(this@SearchActivity, "Erro ao buscar notícias", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                if (editSearch.text.isNotEmpty()) btnClear.visibility = View.VISIBLE
                Toast.makeText(this@SearchActivity, "Verifique sua conexão", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun abrirLink(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Não foi possível abrir o link", Toast.LENGTH_SHORT).show()
        }
    }
}