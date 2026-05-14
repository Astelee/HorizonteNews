package com.horizontenews.app

object Config {
    const val BLOG_ID = "7479654571398345888"

    // ✅ Chave segura vinda do local.properties (BuildConfig)
    const val API_KEY = BuildConfig.BLOGGER_API_KEY

    const val APP_NAME = "Horizonte News"
    const val PRIMARY_COLOR = "#FF6800"
    const val BASE_URL = "https://www.googleapis.com/blogger/v3/"
}