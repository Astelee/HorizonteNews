package com.horizontenews.app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ConfigItem(
    val title: String,
    val subtitle: String? = null,
    val icon: Int,
    val action: () -> Unit
)

class ConfiguracoesAdapter(private val items: List<ConfigItem>) :
    RecyclerView.Adapter<ConfiguracoesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.item_icon)
        val title: TextView = view.findViewById(R.id.item_title)
        val subtitle: TextView = view.findViewById(R.id.item_subtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_configuracao, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.icon.setImageResource(item.icon)

        if (item.subtitle != null) {
            holder.subtitle.text = item.subtitle
            holder.subtitle.visibility = View.VISIBLE
        } else {
            holder.subtitle.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { item.action() }
    }

    override fun getItemCount() = items.size
}