package com.horizontenews.app

import android.os.Bundle
import android.webkit.WebSettings
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

        // Configurações avançadas para o WebView
        val settings = webView.settings
        settings.javaScriptEnabled = true // Ativa o JS para o site funcionar
        settings.domStorageEnabled = true // Importante: permite carregar elementos modernos do site
        settings.databaseEnabled = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false // Esconde os botões de zoom na tela
        
        // "Disfarça" o app como um navegador Chrome para evitar bloqueios ou pedidos de login
        settings.userAgentString = "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Mobile Safari/537.36"

        // Garante que o link abra dentro do seu app e não no navegador externo
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false // Diz ao Android para carregar o link no próprio WebView
            }
        }
        
        if (url != null) {
            webView.loadUrl(url)
        }
    }

    // Opcional: permite que o usuário use o botão "voltar" do celular para navegar no site
    override fun onBackPressed() {
        val webView: WebView = findViewById(R.id.webView)
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}