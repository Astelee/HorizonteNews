package com.horizontenews.app

import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o layout activity_detail.xml que contém o WebView com id webView
        setContentView(R.layout.activity_detail)

        // Referencia o WebView do layout
        val webView: WebView = findViewById(R.id.webView)

        // URL da postagem vinda da Intent
        val url = intent.getStringExtra("postUrl")

        // Configurações do WebView
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.loadsImagesAutomatically = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(false)

        // User Agent para evitar problemas de visualização mobile do Blogger
        settings.userAgentString =
            "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Mobile Safari/537.36"

        // Script para limpar elementos indesejados (Header, Footer, Sidebar, etc)
        val cleanBlogScript = """
            javascript:(function() {
                var style = document.createElement('style');
                style.innerHTML = `
                header, .header, .header-outer, .header-inner, .mobile-header,
                .centered-top-container, .centered-top, .top-bar, .top-header,
                .Header, .Header h1, .Header .descriptionwrapper, .header-widget,
                .search, .search-bar, .search-tab, .search-expand, .search-icon, #search,
                nav, .navigation, .tabs, .tabs-inner, .tabs-outer, .menu,
                .share-buttons, .sharing, .post-share-buttons, .share-button, .post-icons, .byline.share,
                #comments, .comments, .comment-form, .comment-thread, #comment-holder, #comment-editor,
                iframe[src*="comment"], iframe[src*="accounts.google.com"],
                footer, .footer, .footer-outer, .footer-inner, .footer-section,
                .copyright, .credit, .Attribution, .Attribution1, .Attribution2,
                #navbar, #navbar-iframe, .navbar, .navbar-iframe,
                .sidebar, .sidebar-container, .sidebar-wrapper {
                    display: none !important;
                    visibility: hidden !important;
                    height: 0 !important;
                    max-height: 0 !important;
                    opacity: 0 !important;
                    overflow: hidden !important;
                }
                body {
                    margin: 0 !important;
                    padding: 0 !important;
                    background: #ffffff !important;
                    overflow-x: hidden !important;
                }
                .main-wrapper, .main-inner, .content, .content-outer, .content-inner,
                .post, .post-outer, .post-body-container, .post-body {
                    width: 100% !important;
                    max-width: 100% !important;
                    margin: 0 !important;
                    padding: 0 !important;
                }
                img {
                    max-width: 100% !important;
                    height: auto !important;
                    border-radius: 0 !important;
                }
                `;
                document.head.appendChild(style);

                function removeElements() {
                    var selectors = [
                        'header', '.header', '.header-outer', '.mobile-header',
                        '.search', '.search-bar', '.share-buttons', '.sharing',
                        '#comments', '.comments', 'footer', '.footer',
                        '.Attribution', '#navbar', '#navbar-iframe', '.sidebar'
                    ];
                    selectors.forEach(function(selector) {
                        document.querySelectorAll(selector).forEach(function(el) {
                            el.remove();
                        });
                    });
                }
                removeElements();
                var observer = new MutationObserver(function() { removeElements(); });
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

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view?.evaluateJavascript(cleanBlogScript, null)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.evaluateJavascript(cleanBlogScript, null)
            }
        }

        // Carrega a URL se ela não for nula
        url?.let {
            webView.loadUrl(it)
        }
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