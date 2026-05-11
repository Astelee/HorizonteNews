package com.horizontenews.app

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DetailActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        toolbar = findViewById(R.id.toolbar)
        webView = findViewById(R.id.webView)

        // Configura a Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Recebe os dados da notícia
        val postTitle    = intent.getStringExtra("postTitle")    ?: ""
        val postContent  = intent.getStringExtra("postContent")  ?: ""
        val postDate     = intent.getStringExtra("postDate")     ?: ""
        val postCategory = intent.getStringExtra("postCategory") ?: "NOTÍCIA"

        // Configurações do WebView
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = false
        settings.loadsImagesAutomatically = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(false)

        webView.webViewClient = WebViewClient()

        // HTML moderno e limpo
        val htmlContent = """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body {
                        font-family: -apple-system, system-ui, sans-serif;
                        background: #ffffff;
                        color: #1a1a1a;
                        line-height: 1.75;
                        font-size: 16.5px;
                        padding: 20px 16px;
                    }
                    .category {
                        color: #E87722;
                        font-size: 13px;
                        font-weight: 700;
                        text-transform: uppercase;
                        letter-spacing: 1.2px;
                        margin-bottom: 8px;
                    }
                    h1 {
                        font-size: 24px;
                        font-weight: 800;
                        line-height: 1.25;
                        margin-bottom: 12px;
                        color: #111111;
                    }
                    .date {
                        font-size: 14px;
                        color: #777777;
                        margin-bottom: 28px;
                        padding-bottom: 16px;
                        border-bottom: 1px solid #eeeeee;
                    }
                    .content img {
                        max-width: 100% !important;
                        height: auto !important;
                        border-radius: 12px;
                        margin: 20px 0;
                        display: block;
                    }
                    .content p { margin-bottom: 18px; }
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

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    // Botão voltar físico + botão na toolbar
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}