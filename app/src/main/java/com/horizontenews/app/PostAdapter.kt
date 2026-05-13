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

    private val TYPE_HIGHLIGHT = 0
    private val TYPE_NORMAL = 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HIGHLIGHT else TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_HIGHLIGHT) {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_post_highlight, parent, false)

            HighlightViewHolder(view)

        } else {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_post, parent, false)

            NormalViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val post = posts[position]

        if (holder is HighlightViewHolder) {

            holder.title.text = post.title
            holder.category.text = post.firstLabel.uppercase()

            Glide.with(holder.itemView.context)
                .load(post.firstImage)
                .centerCrop()
                .into(holder.image)

        } else if (holder is NormalViewHolder) {

            holder.title.text = post.title
            holder.category.text = post.firstLabel.uppercase()
            holder.date.text = post.published

            Glide.with(holder.itemView.context)
                .load(post.firstImage)
                .centerCrop()
                .into(holder.image)
        }

        holder.itemView.setOnClickListener {

            val context = holder.itemView.context

            val intent = Intent(context, DetailActivity::class.java).apply {

                putExtra("postTitle", post.title)
                putExtra("postContent", post.content)
                putExtra("postImage", post.firstImage)
                putExtra("postDate", post.published)
                putExtra("postCategory", post.firstLabel)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = posts.size

    class HighlightViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.findViewById(R.id.tvTitleHighlight)
        val category: TextView = view.findViewById(R.id.tvCategoryHighlight)
        val image: ImageView = view.findViewById(R.id.ivHighlight)
    }

    class NormalViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.findViewById(R.id.postTitle)
        val category: TextView = view.findViewById(R.id.postCategory)
        val date: TextView = view.findViewById(R.id.postDate)
        val image: ImageView = view.findViewById(R.id.postImage)
    }
}