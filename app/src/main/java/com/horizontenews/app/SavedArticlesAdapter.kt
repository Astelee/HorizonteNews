package com.horizontenews.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SavedArticlesAdapter(

    private var articles: List<SavedArticle>,

    private val onItemClick: (SavedArticle) -> Unit

) : RecyclerView.Adapter<SavedArticlesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_saved_article,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val article = articles[position]

        holder.bind(article)

        holder.itemView.setOnClickListener {

            onItemClick(article)
        }
    }

    override fun getItemCount(): Int {

        return articles.size
    }

    fun updateArticles(
        newArticles: List<SavedArticle>
    ) {

        articles = newArticles

        notifyDataSetChanged()
    }

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val ivImage: ImageView =
            itemView.findViewById(R.id.ivImage)

        private val tvCategory: TextView =
            itemView.findViewById(R.id.tvCategory)

        private val tvTitle: TextView =
            itemView.findViewById(R.id.tvTitle)

        private val tvDate: TextView =
            itemView.findViewById(R.id.tvDate)

        fun bind(article: SavedArticle) {

            tvTitle.text = article.title

            tvCategory.text =
                article.category.uppercase()

            tvDate.text =
                "Salvo • ${article.date}"

            Glide.with(itemView.context)
                .load(article.imageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.darker_gray)
                .centerCrop()
                .into(ivImage)
        }
    }
}