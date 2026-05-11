package com.horizontenews.app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.Locale

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category: TextView = view.findViewById(R.id.postCategory)
        val title: TextView = view.findViewById(R.id.postTitle)
        val date: TextView = view.findViewById(R.id.postDate)
        val image: ImageView = view.findViewById(R.id.postImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]

        holder.title.text = post.title

        val categoryText = post.labels?.firstOrNull()?.uppercase() ?: "NOTÍCIA"
        holder.category.text = categoryText

        // Formata data
        val formattedDate: String = try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(post.published)
            date?.let { outputFormat.format(it) } ?: post.published
        } catch (e: Exception) {
            post.published
        }
        holder.date.text = formattedDate

        // Extrai primeira imagem
        val document = Jsoup.parse(post.content)
        val imageUrl = document.select("img").firstOrNull()?.attr("src")

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image) // crie esse drawable depois
            .centerCrop()
            .into(holder.image)

        // Clique abre detalhe
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("postTitle", post.title)
                putExtra("postContent", post.content)
                putExtra("postDate", formattedDate)
                putExtra("postCategory", categoryText)
                putExtra("postImage", imageUrl)
                putExtra("postUrl", post.url)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = posts.size
}