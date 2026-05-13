package com.horizontenews.app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Define qual layout usar (Destaque para o primeiro, Normal para os outros)
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

        if (holder is HighlightViewHolder) {
            holder.title.text = post.title
            holder.category.text = post.firstLabel().uppercase() // Adicionado ()
            holder.date.text = tempoRelativo
            Glide.with(holder.itemView.context)
                .load(post.firstImage()) // Adicionado ()
                .centerCrop()
                .into(holder.image)
        } else if (holder is NormalViewHolder) {
            holder.title.text = post.title
            holder.category.text = post.firstLabel().uppercase() // Adicionado ()
            holder.date.text = tempoRelativo
            Glide.with(holder.itemView.context)
                .load(post.firstImage()) // Adicionado ()
                .centerCrop()
                .into(holder.image)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("postTitle", post.title)
                putExtra("postContent", post.content)
                putExtra("postImage", post.firstImage()) // Adicionado ()
                putExtra("postDate", tempoRelativo)   
                putExtra("postCategory", post.firstLabel()) // Adicionado ()
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = posts.size

    class HighlightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitleHighlight)
        val category: TextView = view.findViewById(R.id.tvCategoryHighlight)
        val image: ImageView = view.findViewById(R.id.ivHighlight)
        val date: TextView = view.findViewById(R.id.tvDateHighlight) 
    }

    class NormalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.postTitle) 
        val category: TextView = view.findViewById(R.id.postCategory)
        val image: ImageView = view.findViewById(R.id.postImage)
        val date: TextView = view.findViewById(R.id.postDate) 
    }
}