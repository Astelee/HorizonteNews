private fun performSearch(query: String) {
    progressBar.visibility = View.VISIBLE
    btnClear.visibility = View.GONE
    tvEmpty.visibility = View.GONE
    layoutSocial.visibility = View.GONE

    val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/blogger/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(BloggerService::class.java)

    service.searchPosts(Config.BLOG_ID, query, Config.API_KEY).enqueue(object : Callback<PostResponse> {
        override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
            progressBar.visibility = View.GONE

            if (editSearch.text.isNotEmpty()) {
                btnClear.visibility = View.VISIBLE
            }

            if (response.isSuccessful) {
                val posts = response.body()?.items ?: emptyList()

                if (posts.isNotEmpty()) {
                    tvEmpty.visibility = View.GONE
                    // CORREÇÃO AQUI: Adicionado o parâmetro onItemClick
                    val adapter = PostAdapter(
                        posts = posts,
                        onSaveClick = { post, isSaved, callback ->
                            if (isSaved) savePost(post, callback) else unsavePost(post, callback)
                        },
                        getSavedStatus = { post -> runBlocking { checkIfSaved(post) } },
                        onItemClick = { post ->
                            val intent = Intent(this@SearchActivity, DetailActivity::class.java).apply {
                                putExtra("postTitle", post.title)
                                putExtra("postContent", post.content)
                                putExtra("postImage", post.firstImage())
                                putExtra("postDate", post.getTempoRelativo())
                            }
                            startActivity(intent)
                        }
                    )
                    recyclerView.adapter = adapter
                } else {
                    tvEmpty.visibility = View.VISIBLE
                    // CORREÇÃO AQUI TAMBÉM: Para o caso de lista vazia
                    recyclerView.adapter = PostAdapter(
                        posts = emptyList(),
                        onSaveClick = { _, _, callback -> callback(false) },
                        getSavedStatus = { false },
                        onItemClick = {} 
                    )
                }
            } else {
                Toast.makeText(this@SearchActivity, "Erro ao buscar notícias", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<PostResponse>, t: Throwable) {
            progressBar.visibility = View.GONE
            if (editSearch.text.isNotEmpty()) btnClear.visibility = View.VISIBLE
            Toast.makeText(this@SearchActivity, "Verifique sua conexão", Toast.LENGTH_SHORT).show()
        }
    })
}