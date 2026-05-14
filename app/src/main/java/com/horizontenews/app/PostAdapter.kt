package com.astelee.horizontenews.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.astelee.horizontenews.databinding.ItemPostBinding
import com.astelee.horizontenews.model.Article
import com.bumptech.glide.Glide

class PostAdapter(
    private val onItemClick: (Article) -> Unit
) : ListAdapter<Article, PostAdapter.PostViewHolder>(ArticleDiffCallback()) {

    inner class PostViewHolder(private val binding: ItemPostBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(article: Article) {
            binding.tvPostTitle.text = article.title
            binding.tvPostDate.text = article.publishedAt ?: "Data não disponível"

            // Carregar imagem
            article.urlToImage?.let { url ->
                Glide.with(binding.root.context)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_news)
                    .error(R.drawable.placeholder_news)
                    .into(binding.ivPostImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url  // ou id se você tiver
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.title == newItem.title && 
               oldItem.publishedAt == newItem.publishedAt
    }
}