package com.horizontenews.app

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var editSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Inicializa componentes
        recyclerView = findViewById(R.id.recyclerViewSearch)
        editSearch = findViewById(R.id.edit_search)
        val btnBack = findViewById<ImageButton>(R.id.btn_back_search)
        val btnDoSearch = findViewById<ImageButton>(R.id.btn_do_search)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Botão voltar
        btnBack.setOnClickListener {
            finish()
        }

        // Botão que executa a busca
        btnDoSearch.setOnClickListener {
            val query = editSearch.text.toString()
            if (query.isNotEmpty()) {
                performSearch(query)
            }
        }
    }

    private fun performSearch(query: String) {
        // Aqui vai a sua lógica de busca no Blogger futuramente
    }
}