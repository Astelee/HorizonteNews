package com.horizontenews.app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class PostAdapter(
    private var posts: List<Post>,
    private val onSaveClick: (Post, Boolean, (Boolean) -> Unit) -> Unit,
    private val getSavedStatus: (Post) -> Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val savedStatusCache = mutableMapOf<String, Boolean>()
    private val loadingStatus = mutableMapOf<String, Boolean>()

    init {
        posts.forEach { post ->
            savedStatusCache[post.url] = getSavedStatus(post)
            loadingStatus[post.url] = false
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
        val isLoading = loadingStatus[post.url] ?: false

        if (holder is HighlightViewHolder) {
            holder.title.text = post.title
            holder.category.text = post.firstLabel().uppercase()
            holder.date.text = tempoRelativo
            
            Glide.with(holder.itemView.context)
                .load(post.firstImage())
                .centerCrop()
                .placeholder(android.R.color.darker_gray)
                .into(holder.image)

            updateSaveButtonState(holder.btnSave, holder.progressBar, isSaved, isLoading)
            
            holder.btnSave.setOnClickListener {
                if (!isLoading) {
                    val newStatus = !isSaved
                    loadingStatus[post.url] = true
                    updateSaveButtonState(holder.btnSave, holder.progressBar, isSaved, true)
                    
                    onSaveClick(post, newStatus) { success ->
                        loadingStatus[post.url] = false
                        if (success) {
                            savedStatusCache[post.url] = newStatus
                            updateSaveButtonState(holder.btnSave, holder.progressBar, newStatus, false)
                            
                            // Mostrar Snackbar com botão
                            val message = if (newStatus) {
                                "✅ Foi adicionado na lista de salvos"
                            } else {
                                "🗑️ Removido da lista de salvos"
                            }
                            
                            Snackbar.make(holder.itemView, message, Snackbar.LENGTH_LONG)
                                .setAction("VER") {
                                    val intent = Intent(holder.itemView.context, SavedArticlesActivity::class.java)
                                    holder.itemView.context.startActivity(intent)
                                }
                                .setActionTextColor(holder.itemView.context.getColor(R.color.orange))
                                .show()
                        } else {
                            updateSaveButtonState(holder.btnSave, holder.progressBar, isSaved, false)
                            Snackbar.make(holder.itemView, "Erro ao salvar. Tente novamente.", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
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

            updateSaveButtonState(holder.btnSave, holder.progressBar, isSaved, isLoading)
            
            holder.btnSave.setOnClickListener {
                if (!isLoading) {
                    val newStatus = !isSaved
                    loadingStatus[post.url] = true
                    updateSaveButtonState(holder.btnSave, holder.progressBar, isSaved, true)
                    
                    onSaveClick(post, newStatus) { success ->
                        loadingStatus[post.url] = false
                        if (success) {
                            savedStatusCache[post.url] = newStatus
                            updateSaveButtonState(holder.btnSave, holder.progressBar, newStatus, false)
                            
                            val message = if (newStatus) {
                                "✅ Foi adicionado na lista de salvos"
                            } else {
                                "🗑️ Removido da lista de salvos"
                            }
                            
                            Snackbar.make(holder.itemView, message, Snackbar.LENGTH_LONG)
                                .setAction("VER") {
                                    val intent = Intent(holder.itemView.context, SavedArticlesActivity::class.java)
                                    holder.itemView.context.startActivity(intent)
                                }
                                .setActionTextColor(holder.itemView.context.getColor(R.color.orange))
                                .show()
                        } else {
                            updateSaveButtonState(holder.btnSave, holder.progressBar, isSaved, false)
                            Snackbar.make(holder.itemView, "Erro ao salvar. Tente novamente.", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
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

    private fun updateSaveButtonState(button: ImageButton, progressBar: ProgressBar, isSaved: Boolean, isLoading: Boolean) {
        if (isLoading) {
            button.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            button.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            if (isSaved) {
                button.setImageResource(R.drawable.ic_bookmark_filled)
            } else {
                button.setImageResource(R.drawable.ic_bookmark_border)
            }
        }
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        this.posts = newPosts
        savedStatusCache.clear()
        loadingStatus.clear()
        posts.forEach { post ->
            savedStatusCache[post.url] = getSavedStatus(post)
            loadingStatus[post.url] = false
        }
        notifyDataSetChanged()
    }

    fun refreshSavedStatus() {
        posts.forEach { post ->
            savedStatusCache[post.url] = getSavedStatus(post)
        }
        notifyDataSetChanged()
    }

    class HighlightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitleHighlight)
        val category: TextView = view.findViewById(R.id.tvCategoryHighlight)
        val image: ImageView = view.findViewById(R.id.ivHighlight)
        val date: TextView = view.findViewById(R.id.tvDateHighlight)
        val btnSave: ImageButton = view.findViewById(R.id.btnSaveHighlight)
        val progressBar: ProgressBar = view.findViewById(R.id.progressSaveHighlight)
    }

    class NormalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.postTitle)
        val category: TextView = view.findViewById(R.id.postCategory)
        val image: ImageView = view.findViewById(R.id.postImage)
        val date: TextView = view.findViewById(R.id.postDate)
        val btnSave: ImageButton = view.findViewById(R.id.btnSave)
        val progressBar: ProgressBar = view.findViewById(R.id.progressSave)
    }
}