package com.horizontenews.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.horizontenews.app.databinding.ItemSavedArticleBinding

class SavedArticlesAdapter(
    private var articles: List<SavedArticle>,
    private val onItemClick: (SavedArticle) -> Unit
) : RecyclerView.Adapter<SavedArticlesAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavedArticleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
        holder.itemView.setOnClickListener { onItemClick(article) }
    }
    
    override fun getItemCount() = articles.size
    
    fun updateArticles(newArticles: List<SavedArticle>) {
        articles = newArticles
        notifyDataSetChanged()
    }
    
    class ViewHolder(private val binding: ItemSavedArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: SavedArticle) {
            binding.tvTitle.text = article.title
            binding.tvCategory.text = article.category
            binding.tvDate.text = article.date
            Glide.with(binding.root.context)
                .load(article.imageUrl)
                .placeholder(android.R.color.darker_gray)
                .into(binding.ivImage)
        }
    }
}