package com.horizontenews.app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PostAdapter(
    private var posts: List<Post>,
    private val onSaveClick: (Post, Boolean) -> Unit,
    private val getSavedStatus: (Post) -> Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val savedStatusCache = mutableMapOf<String, Boolean>()

    init {
        posts.forEach { post ->
            savedStatusCache[post.url] = runBlocking { getSavedStatus(post) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_highlight, parent, false)
            HighlightViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
            NormalViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = posts[position]
        val tempoRelativo = post.getTempoRelativo()
        val isSaved = savedStatusCache[post.url] ?: false

        if (holder is HighlightViewHolder) {
            holder.title.text = post.title
            holder.category.text = post.firstLabel().uppercase()
            holder.date.text = tempoRelativo
            
            Glide.with(holder.itemView.context)
                .load(post.firstImage())
                .centerCrop()
                .placeholder(android.R.color.darker_gray)
                .into(holder.image)

            // Configurar botão salvar
            updateSaveButtonIcon(holder.btnSave, isSaved)
            holder.btnSave.setOnClickListener {
                val newStatus = !isSaved
                savedStatusCache[post.url] = newStatus
                onSaveClick(post, newStatus)
                updateSaveButtonIcon(holder.btnSave, newStatus)
            }

        } else if (holder is NormalViewHolder) {
            holder.title.text = post.title
            holder.category.text = post.firstLabel().uppercase()
            holder.date.text = tempoRelativo
            
            Glide.with(holder.itemView.context)
                .load(post.firstImage())
                .centerCrop()
                .placeholder(android.R.color.darker_gray)
                .into(holder.image)

            // Configurar botão salvar
            updateSaveButtonIcon(holder.btnSave, isSaved)
            holder.btnSave.setOnClickListener {
                val newStatus = !isSaved
                savedStatusCache[post.url] = newStatus
                onSaveClick(post, newStatus)
                updateSaveButtonIcon(holder.btnSave, newStatus)
            }
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("postTitle", post.title)
                putExtra("postContent", post.content)
                putExtra("postImage", post.firstImage())
                putExtra("postDate", tempoRelativo)
                putExtra("postCategory", post.firstLabel())
            }
            context.startActivity(intent)
        }
    }

    private fun updateSaveButtonIcon(button: ImageButton, isSaved: Boolean) {
        if (isSaved) {
            button.setImageResource(R.drawable.ic_bookmark_filled)
        } else {
            button.setImageResource(R.drawable.ic_bookmark_border)
        }
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        this.posts = newPosts
        savedStatusCache.clear()
        posts.forEach { post ->
            CoroutineScope(Dispatchers.IO).launch {
                val status = getSavedStatus(post)
                savedStatusCache[post.url] = status
            }
        }
        notifyDataSetChanged()
    }

    class HighlightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitleHighlight)
        val category: TextView = view.findViewById(R.id.tvCategoryHighlight)
        val image: ImageView = view.findViewById(R.id.ivHighlight)
        val date: TextView = view.findViewById(R.id.tvDateHighlight)
        val btnSave: ImageButton = view.findViewById(R.id.btnSaveHighlight)
    }

    class NormalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.postTitle)
        val category: TextView = view.findViewById(R.id.postCategory)
        val image: ImageView = view.findViewById(R.id.postImage)
        val date: TextView = view.findViewById(R.id.postDate)
        val btnSave: ImageButton = view.findViewById(R.id.btnSave)
    }
}