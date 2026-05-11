package com.horizontenews.app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BloggerService {

    @GET("blogs/${Config.BLOG_ID}/posts")
    fun getPosts(@Query("key") apiKey: String): Call<PostResponse>

}