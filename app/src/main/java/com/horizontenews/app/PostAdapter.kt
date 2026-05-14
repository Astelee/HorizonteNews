package com.horizontenews.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(
    private var posts: List<Post>,
    private val onSaveClick: (Post, Boolean, (Boolean) -> Unit) -> Unit,
    private val getSavedStatus: (Post) -> Boolean,
    private val onItemClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Usando findViewById para garantir compatibilidade com seu XML atual
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_post_title)
        private val ivImage: ImageView = itemView.findViewById(R.id.iv_post_image)
        private val btnSave: ImageButton = itemView.findViewById(R.id.btn_save) // Certifique-se que este ID existe no item_post.xml

        fun bind(post: Post) {
            tvTitle.text = post.title
            
            // Carrega a imagem da notícia
            Glide.with(itemView.context)
                .load(post.firstImage())
                .placeholder(android.R.color.darker_gray)
                .centerCrop()
                .into(ivImage)

            // Lógica do botão Salvar (Favoritos)
            var isSaved = getSavedStatus(post)
            updateSaveIcon(btnSave, isSaved)

            btnSave.setOnClickListener {
                val newStatus = !isSaved
                onSaveClick(post, newStatus) { success ->
                    if (success) {
                        isSaved = newStatus
                        updateSaveIcon(btnSave, isSaved)
                    }
                }
            }

            // Clique na notícia para abrir detalhes
            itemView.setOnClickListener {
                onItemClick(post)
            }
        }
    }

    private fun updateSaveIcon(button: ImageButton, isSaved: Boolean) {
        val icon = if (isSaved) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
        button.setImageResource(icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    // Funções que a sua MainActivity chama para atualizar a lista
    fun updatePosts(newPosts: List<Post>) {
        this.posts = newPosts
        notifyDataSetChanged()
    }

    fun refreshSavedStatus() {
        notifyDataSetChanged()
    }
}