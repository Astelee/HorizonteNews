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

        val postTitle = intent.getStringExtra("postTitle") ?: ""
        val postContent = intent.getStringExtra("postContent") ?: ""
        val postDate = intent.getStringExtra("postDate") ?: ""
        val postCategory = intent.getStringExtra("postCategory") ?: "NOTÍCIA"

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = false
        settings.loadsImagesAutomatically = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body {
                        font-family: -apple-system, system-ui, BlinkMacSystemFont, sans-serif;
                        background: #ffffff;
                        color: #1a1a1a;
                        line-height: 1.7;
                        font-size: 16.5px;
                        padding: 16px;
                    }
                    .category {
                        color: #E87722;
                        font-size: 13px;
                        font-weight: 700;
                        text-transform: uppercase;
                        letter-spacing: 1px;
                        margin-bottom: 8px;
                    }
                    h1 {
                        font-size: 24px;
                        font-weight: 800;
                        line-height: 1.25;
                        margin-bottom: 12px;
                        color: #111;
                    }
                    .date {
                        color: #777;
                        font-size: 14px;
                        margin-bottom: 24px;
                        padding-bottom: 16px;
                        border-bottom: 1px solid #eee;
                    }
                    .content img {
                        max-width: 100% !important;
                        height: auto !important;
                        border-radius: 12px;
                        margin: 20px 0;
                    }
                    .content p { margin-bottom: 18px; }
                    .content a { color: #E87722; }
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

    override fun onBackPressed() {
        val webView: WebView = findViewById(R.id.webView)
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}