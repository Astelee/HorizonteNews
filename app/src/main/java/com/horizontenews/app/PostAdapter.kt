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
        val categoryText = post.labels?.firstOrNull()?.uppercase() ?: "NOTÍCIA"
        holder.category.text = categoryText

        // 3. Formata a Data
        val formattedDate: String = try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(post.published)
            date?.let { outputFormat.format(it) } ?: post.published
        } catch (e: Exception) {
            post.published
        }
        holder.date.text = formattedDate

        // 4. Carrega a Imagem
        val document = Jsoup.parse(post.content)
        val imageUrl = document.select("img").firstOrNull()?.attr("src")

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(android.R.color.darker_gray)
            .centerCrop()
            .into(holder.image)

        // --- 5. AÇÃO DE CLIQUE: agora abre a DetailActivity com conteúdo LIMPO ---
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val intent = Intent(context, DetailActivity::class.java).apply {
                // Em vez de só a URL, passamos o conteúdo direto da API do Blogger
                putExtra("postTitle", post.title)
                putExtra("postContent", post.content)
                putExtra("postDate", formattedDate)
                putExtra("postCategory", categoryText)
                putExtra("postImage", imageUrl)
                putExtra("postUrl", post.url) // mantido caso queira usar para "compartilhar"
            }

            context.startActivity(intent)
        }
    }

    override fun getItemC