package com.horizontenews.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.jsoup.Jsoup

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.postTitle)
        val image: ImageView = view.findViewById(R.id.postImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.title.text = post.title
        
        // Pega a primeira imagem do conteúdo HTML da notícia
        val document = Jsoup.parse(post.content)
        val imageUrl = document.select("img").firstOrNull()?.attr("src")

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(android.R.color.darker_gray)
            .into(holder.image)
    }

    override fun getItemCount() = posts.size
}
