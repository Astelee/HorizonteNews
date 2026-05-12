package com.horizontenews.app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BloggerService {
    // Busca todos os posts da página inicial
    @GET("blogs/{blogId}/posts")
    fun getPosts(
        @Path("blogId") blogId: String,
        @Query("key") apiKey: String
    ): Call<PostResponse>

    // NOVA FUNÇÃO: Busca posts por palavra-chave (Lupa)
    @GET("blogs/{blogId}/posts/search")
    fun searchPosts(
        @Path("blogId") blogId: String,
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): Call<PostResponse>
}