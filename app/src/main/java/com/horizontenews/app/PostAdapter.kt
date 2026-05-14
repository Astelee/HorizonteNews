package com.horizontenews.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(
    private var posts: List<Post>,
    private val onItemClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_post_title)
        val ivImage: ImageView = itemView.findViewById(R.id.iv_post_image)
        val tvDate: TextView = itemView.findViewById(R.id.tv_post_date)

        fun bind(post: Post) {
            tvTitle.text = post.title
            tvDate.text = post.getTempoRelativo()
            
            Glide.with(itemView.context)
                .load(post.firstImage())
                .placeholder(android.R.color.darker_gray)
                .centerCrop()
                .into(ivImage)

            itemView.setOnClickListener { onItemClick(post) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        this.posts = newPosts
        notifyDataSetChanged()
    }
}