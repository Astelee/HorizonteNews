package com.horizontenews.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.horizontenews.app.databinding.ItemPostHighlightBinding
import com.horizontenews.app.databinding.ItemPostBinding

class PostAdapter(
    private val posts: List<Post>,
    private val onItemClick: (Post) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HIGHLIGHT = 0
        private const val VIEW_TYPE_NORMAL = 1
    }

    override fun getItemViewType(position: Int): Int {
        // A primeira notícia (posição 0) será o destaque
        return if (position == 0) VIEW_TYPE_HIGHLIGHT else VIEW_TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_HIGHLIGHT) {
            val binding = ItemPostHighlightBinding.inflate(inflater, parent, false)
            HighlightViewHolder(binding)
        } else {
            val binding = ItemPostBinding.inflate(inflater, parent, false)
            NormalViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = posts[position]
        when (holder) {
            is HighlightViewHolder -> holder.bind(post)
            is NormalViewHolder -> holder.bind(post)
        }
    }

    override fun getItemCount() = posts.size

    // ViewHolder para a notícia principal (Destaque)
    inner class HighlightViewHolder(private val binding: ItemPostHighlightBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.tvTitle.text = post.title
            binding.tvDate.text = post.getTempoRelativo() // Usando o método que criamos 📅
            binding.tvLabel.text = post.firstLabel()      // Usando a categoria 🏷️

            Glide.with(binding.root.context)
                .load(post.firstImage() ?: R.drawable.ic_placeholder) // Busca a imagem no HTML 🖼️
                .into(binding.ivThumbnail)

            binding.root.setOnClickListener { onItemClick(post) }
        }
    }

    // ViewHolder para as notícias da lista comum
    inner class NormalViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.tvTitle.text = post.title
            binding.tvDate.text = post.getTempoRelativo()
            
            // Tenta carregar a imagem do post, se não houver, usa o placeholder
            Glide.with(binding.root.context)
                .load(post.firstImage() ?: R.drawable.ic_placeholder)
                .centerCrop()
                .into(binding.ivThumbnail)

            binding.root.setOnClickListener { onItemClick(post) }
        }
    }
}