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
        val title: TextView = view.findViewById(R.id.postTitle)
        val image: ImageView = view.findViewById(R.id.postImage)
        val category: TextView = view.findViewById(R.id.postCategory)
        val date: TextView = view.findViewById(R.id.postDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        
        // 1. Define o Título
        holder.title.text = post.title
        
        // 2. Define a Categoria
        holder.category.text = post.labels?.firstOrNull()?.uppercase() ?: "NOTÍCIA"
        
        // 3. Formata a Data
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(post.published)
            holder.date.text = date?.let { outputFormat.format(it) } ?: post.published
        } catch (e: Exception) {
            holder.date.text = post.published
        }
        
        // 4. Carrega a Imagem
        val document = Jsoup.parse(post.content)
        val imageUrl = document.select("img").firstOrNull()?.attr("src")

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(android.R.color.darker_gray)
            .centerCrop()
            .into(holder.image)

        // --- 5. AÇÃO DE CLIQUE: O QUE ESTAVA FALTANDO ---
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            
            // Criamos a Intent para abrir a DetailActivity
            val intent = Intent(context, DetailActivity::class.java)
            
            // Passamos a URL da notícia para a próxima tela
            intent.putExtra("postUrl", post.url)
            
            // Iniciamos a transição de tela
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = posts.size
}