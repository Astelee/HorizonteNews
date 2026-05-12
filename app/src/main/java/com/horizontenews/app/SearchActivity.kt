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
        val btnInsta = findViewById<ImageButton>(R.id.btn_insta)
        val btnWhats = findViewById<ImageButton>(R.id.btn_whatsapp)
        val btnFace = findViewById<ImageButton>(R.id.btn_facebook)

        recyclerView.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener { finish() }

        // Links do Horizonte News
        btnInsta.setOnClickListener { 
            abrirLink("https://www.instagram.com/horizontenews_/") 
        }

        btnWhats.setOnClickListener { 
            abrirLink("https://wa.me/5585994130806") 
        }

        btnFace.setOnClickListener { 
            abrirLink("https://www.facebook.com/share/1AJNBnodHo/") 
        }
        
        // Simulação de busca para ocultar/mostrar redes sociais
        // (Isso depende de como sua lógica de busca está implementada)
    }

    private fun abrirLink(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao abrir link", Toast.LENGTH_SHORT).show()
        }
    }
}