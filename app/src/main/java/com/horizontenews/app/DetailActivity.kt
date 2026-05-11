val hideLoginScript = """
    javascript:(function() {

        var style = document.createElement('style');
        style.innerHTML = `

        /* REMOVE HEADER COMPLETO */
        header,
        .header-outer,
        .header-inner,
        .Header,
        .Header h1,
        .Header .descriptionwrapper,
        .mobile-header,
        .centered-top,
        .top-bar,
        .search,
        .search-bar,
        .search-tab,
        .search-expand,
        .search-icon,
        .header-widget,
        .nav-wrapper,
        .navigation,
        .tabs-inner,
        .sidebar-container,
        .dim-overlay,

        /* REMOVE BOTÕES */
        .share-buttons,
        .sharing,
        .post-share-buttons,
        .share-button,
        .post-icons,
        .byline.share,

        /* REMOVE COMENTÁRIOS */
        #comments,
        .comments,
        .comment-form,
        .comment-thread,
        #comment-holder,
        #comment-editor,
        iframe[src*="comment"],

        /* REMOVE FOOTER */
        footer,
        .footer,
        .footer-outer,
        .footer-inner,
        .footer-section,
        .credit,
        .copyright,
        .Attribution,
        .Attribution1,
        .Attribution2,

        /* REMOVE NAVBAR BLOGGER */
        #navbar,
        #navbar-iframe,
        .navbar,
        .navbar-iframe

        {
            display: none !important;
            visibility: hidden !important;
            height: 0 !important;
            max-height: 0 !important;
            opacity: 0 !important;
            overflow: hidden !important;
        }

        /* AJUSTA A PÁGINA */
        body {
            margin: 0 !important;
            padding: 0 !important;
            background: #ffffff !important;
        }

        .main-wrapper,
        .main-inner,
        .content-outer,
        .content-inner,
        .post-outer,
        .post,
        .post-body {
            margin: 0 !important;
            padding: 0 !important;
            width: 100% !important;
            max-width: 100% !important;
        }

        img {
            max-width: 100% !important;
            height: auto !important;
        }

        `;
        
        document.head.appendChild(style);

        function removeElements() {
            var selectors = [
                'header',
                '.header-outer',
                '.mobile-header',
                '.search',
                '.search-bar',
                '.share-buttons',
                '.sharing',
                '#comments',
                'footer',
                '.footer',
                '.Attribution',
                '#navbar',
                '#navbar-iframe'
            ];

            selectors.forEach(function(selector) {
                document.querySelectorAll(selector).forEach(function(el) {
                    el.remove();
                });
            });
        }

        removeElements();

        var observer = new MutationObserver(function() {
            removeElements();
        });

        observer.observe(document.body, {
            childList: true,
            subtree: true
        });

    })();
""".trimIndent()