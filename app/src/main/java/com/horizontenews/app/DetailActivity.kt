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

        // Recebe os dados já limpos da API do Blogger
        val postTitle    = intent.getStringExtra("postTitle")    ?: ""
        val postContent  = intent.getStringExtra("postContent")  ?: ""
        val postDate     = intent.getStringExtra("postDate")     ?: ""
        val postCategory = intent.getStringExtra("postCategory") ?: "NOTÍCIA"

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = false
        settings.loadsImagesAutomatically = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(false)

        webView.webViewClient = WebViewClient()

        // Monta HTML limpo — sem nenhum elemento do Blogger
        val htmlContent = """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body {
                        font-family: -apple-system, system-ui, sans-serif;
                        background: #ffffff;
                        color: #1a1a1a;
                        padding: 16px;
                        line-height: 1.8;
                        font-size: 16px;
                    }
                    .category {
                        color: #E87722;
                        font-size: 11px;
                        font-weight: 700;
                        text-transform: uppercase;
                        letter-spacing: 1.5px;
                        margin-bottom: 10px;
                    }
                    h1 {
                        font-size: 22px;
                        font-weight: 800;
                        line-height: 1.3;
                        color: #111111;
                        margin-bottom: 8px;
                    }
                    .date {
                        font-size: 13px;
                        color: #999999;
                        margin-bottom: 20px;
                        padding-bottom: 16px;
                        border-bottom: 1px solid #eeeeee;
                    }
                    .content img {
                        max-width: 100% !important;
                        height: auto !important;
                        border-radius: 8px;
                        margin: 12px 0;
                        display: block;
                    }
                    .content p { margin-bottom: 16px; }
                    .content a { color: #E87722; text-decoration: none; }
                </style>
            </head>
            <body>
                <div class="category">$postCategory</div>
                <h1>$postTitle</h1>
                <div class="date">$postDate</div>
                <div class="content">$postContent</div>
            </body>
            </html>
        """.trimIndent()

        // Renderiza conteúdo direto — impossível aparecer elementos do Blogger
        webView.loadDataWithBaseURL(
            "https://www.blogger.com",
            htmlContent,
            "text/html",
            "UTF-8",
            null
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val webView: WebView = findViewById(R.id.webView)
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}