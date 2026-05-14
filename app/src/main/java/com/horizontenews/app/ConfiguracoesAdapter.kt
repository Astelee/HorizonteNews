package com.horizontenews.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ConfigItem(
    val title: String,
    val subtitle: String? = null,
    val iconRes: Int? = null,
    val action: () -> Unit
)

class ConfiguracoesAdapter(
    private val items: List<ConfigItem>
) : RecyclerView.Adapter<ConfiguracoesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_configuracao, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        
        holder.tvTitle.text = item.title
        holder.tvSubtitle.text = item.subtitle ?: ""
        holder.tvSubtitle.visibility = if (item.subtitle.isNullOrEmpty()) View.GONE else View.VISIBLE

        item.iconRes?.let {
            holder.ivIcon.setImageResource(it)
            holder.ivIcon.visibility = View.VISIBLE
        } ?: run {
            holder.ivIcon.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { item.action() }
    }

    override fun getItemCount() = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.iv_icon)
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvSubtitle: TextView = view.findViewById(R.id.tv_subtitle)
    }
}