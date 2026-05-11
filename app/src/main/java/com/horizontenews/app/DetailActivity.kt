package com.horizontenews.app

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val webView: WebView = findViewById(R.id.webView)
        
        // Recupera o link enviado pelo Adapter
        val url = intent.getStringExtra("postUrl")

        webView.webViewClient = WebViewClient() 
        webView.settings.javaScriptEnabled = true 
        
        if (url != null) {
            webView.loadUrl(url)
        }
    }
}