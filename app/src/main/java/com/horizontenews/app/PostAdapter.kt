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
        return if (position == 0) VIEW_TYPE_HIGHLIGHT else VIEW_TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HIGHLIGHT) {
            val binding = ItemPostHighlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HighlightViewHolder(binding)
        } else {
            val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            NormalViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = posts[position]

        if (holder is HighlightViewHolder) {
            holder.bind(post)
        } else if (holder is NormalViewHolder) {
            holder.bind(post)
        }
    }

    override fun getItemCount() = posts.size

    // ViewHolder para notícia grande
    inner class HighlightViewHolder(private val binding: ItemPostHighlightBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick(posts[adapterPosition])
            }
        }

        fun bind(post: Post) {
            binding.tvTitle.text = post.title
            binding.tvDate.text = post.publishedDate ?: "Ontem"

            Glide.with(binding.root.context)
                .load(post.imageUrl ?: post.url)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.ivThumbnail)
        }
    }

    // ViewHolder para notícias pequenas
    inner class NormalViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick(posts[adapterPosition])
            }
        }

        fun bind(post: Post) {
            binding.tvTitle.text = post.title
            binding.tvDate.text = post.publishedDate ?: "Ontem"

            Glide.with(binding.root.context)
                .load(post.imageUrl ?: post.url)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.ivThumbnail)
        }
    }
}