package com.horizontenews.app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BloggerService {

    // Função para listar todas as notícias (Página Principal)
    @GET("blogs/{blogId}/posts")
    fun getPosts(
        @Path("blogId") blogId: String = Config.BLOG_ID,
        @Query("key") apiKey: String
    ): Call<PostResponse>

    // Função para pesquisar notícias (Tela de Busca)
    @GET("blogs/{blogId}/posts/search")
    fun searchPosts(
        @Path("blogId") blogId: String = Config.BLOG_ID,
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): Call<PostResponse>

}