"package com.horizontenews.app

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
        val url = intent.getStringExtra(\"postUrl\")

        // Configurações avançadas para o WebView
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false

        // \"Disfarça\" o app como um navegador Chrome
        settings.userAgentString =
            \"Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Mobile Safari/537.36\"

        // CSS/JS que escondem a seção de comentários e o botão de login do Blogger
        val hideLoginScript = \"\"\"
            javascript:(function() {
                var style = document.createElement('style');
                style.type = 'text/css';
                style.innerHTML = `
                    /* Esconde a seção de comentários do Blogger */
                    #comments,
                    .comments,
                    #comment-holder,
                    #comment-editor,
                    #comment-editor-src,
                    .comment-form,
                    #cmt_iframe_holder,
                    .comment-thread,
                    .comments-content,
                    #blogger-iframe-colorize,
                    iframe[src*=\"blogger.com/comment\"],
                    iframe[src*=\"accounts.google.com\"],
                    iframe[src*=\"gsi\"],
                    div[id*=\"comment\"],
                    /* Botões/links de login genéricos */
                    a[href*=\"accounts.google.com\"],
                    a[href*=\"ServiceLogin\"],
                    a[href*=\"signin\"],
                    a[href*=\"login\"],
                    button[id*=\"login\"],
                    button[class*=\"login\"],
                    .login-button,
                    .g-signin,
                    .g_id_signin,
                    #credential_picker_container {
                        display: none !important;
                        visibility: hidden !important;
                        height: 0 !important;
                        width: 0 !important;
                        overflow: hidden !important;
                    }
                `;
                document.head.appendChild(style);

                // Remove via JS também (caso CSS não pegue)
                function removeLoginElements() {
                    var selectors = [
                        '#comments', '.comments', '#comment-holder',
                        '#comment-editor', '.comment-form', '#cmt_iframe_holder'
                    ];
                    selectors.forEach(function(sel) {
                        document.querySelectorAll(sel).forEach(function(el) {
                            el.remove();
                        });
                    });
                }
                removeLoginElements();
                // Observa mudanças dinâmicas
                var observer = new MutationObserver(removeLoginElements);
                observer.observe(document.body, { childList: true, subtree: true });
            })();
        \"\"\".trimIndent()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // Bloqueia abrir URLs de login do Google
                if (url != null && (url.contains(\"accounts.google.com\") ||
                            url.contains(\"ServiceLogin\") ||
                            url.contains(\"/signin\") ||
                            url.contains(\"blogger.com/comment\"))) {
                    return true // cancela o carregamento
                }
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Injeta o script depois que a página termina de carregar
                view?.evaluateJavascript(hideLoginScript, null)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Injeta o CSS o quanto antes para evitar o \"flash\" do botão
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