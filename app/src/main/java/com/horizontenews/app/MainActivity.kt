package com.horizontenews.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface BloggerService {
    @GET("blogs/${Config.BLOG_ID}/posts")
    fun getPosts(@Query("key") apiKey: String): Call<PostResponse>
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/blogger/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BloggerService::class.java)
        service.getPosts(Config.API_KEY).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    val posts = response.body()?.items ?: listOf()
                    recyclerView.adapter = PostAdapter(posts) { post ->
                        // Ao clicar no post, abre a tela limpa
                        val intent = Intent(this@MainActivity, PostDetailActivity::class.java)
                        intent.putExtra("title", post.title)
                        intent.putExtra("content", post.content)
                        intent.putExtra("published", post.published)
                        startActivity(intent)
                    }
                }
            }
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {}
        })
    }
}