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
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false

        // "Disfarça" o app como um navegador Chrome
        settings.userAgentString =
            "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Mobile Safari/537.36"

        // CSS/JS atualizado para incluir a remoção da barra cinza e pesquisa
        val hideLoginScript = """
            javascript:(function() {
                var style = document.createElement('style');
                style.type = 'text/css';
                style.innerHTML = `
                    /* 1. Esconde a barra cinza oficial do Blogger (Navbar) */
                    #navbar, .navbar, #navbar-iframe, .navbar-iframe,
                    
                    /* 2. Esconde o cabeçalho cinza e a barra de pesquisa */
                    .header-outer, .header-cap, .Search, #search, .search-bar, .search-tab,
                    
                    /* 3. Esconde a seção de comentários e login (o que você já tinha) */
                    #comments, .comments, #comment-holder, #comment-editor,
                    .comment-form, #cmt_iframe_holder, .comment-thread,
                    iframe[src*="blogger.com/comment"],
                    iframe[src*="accounts.google.com"],
                    #credential_picker_container {
                        display: none !important;
                        visibility: hidden !important;
                        height: 0 !important;
                    }

                    /* Remove espaços extras que sobram no topo */
                    body { padding-top: 0 !important; margin-top: 0 !important; }
                    .main-outer { margin-top: 0 !important; }
                `;
                document.head.appendChild(style);

                // Função para remover elementos via código também
                function cleanBlogger() {
                    var selectors = ['#navbar', '.header-outer', '#comments', '.Search'];
                    selectors.forEach(function(sel) {
                        document.querySelectorAll(sel).forEach(function(el) {
                            el.remove();
                        });
                    });
                }
                cleanBlogger();

                var observer = new MutationObserver(cleanBlogger);
                observer.observe(document.body, { childList: true, subtree: true });
            })();
        """.trimIndent()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && (
                            url.contains("accounts.google.com") ||
                            url.contains("ServiceLogin") ||
                            url.contains("/signin") ||
                            url.contains("blogger.com/comment")
                            )) {
                    return true
                }
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.evaluateJavascript(hideLoginScript, null)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view?.evaluateJavascript(hideLoginScript, null)
            }
        }

        if (url != null) {
            webView.loadUrl(url)
        }
    }

    override fun onBackPressed() {
        val webView: WebView = findViewById(R.id.webView)
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}