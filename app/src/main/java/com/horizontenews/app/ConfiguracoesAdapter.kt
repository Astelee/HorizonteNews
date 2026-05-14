package com.horizontenews.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador para a lista de configurações.
 * Ele utiliza a classe ConfigItem (que deve estar no arquivo ConfigItem.kt).
 */
class ConfiguracoesAdapter(
    private val items: List<ConfigItem>
) : RecyclerView.Adapter<ConfiguracoesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Infla o layout de cada linha da configuração
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_configuracao, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Define o título principal
        holder.tvTitle.text = item.title

        // Configura o subtítulo: se estiver vazio, esconde o componente
        holder.tvSubtitle.text = item.subtitle ?: ""
        holder.tvSubtitle.visibility = if (item.subtitle.isNullOrEmpty()) View.GONE else View.VISIBLE

        // Configura o ícone: se não houver um ID de recurso, esconde o ImageView
        item.iconRes?.let {
            holder.ivIcon.setImageResource(it)
            holder.ivIcon.visibility = View.VISIBLE
        } ?: run {
            holder.ivIcon.visibility = View.GONE
        }

        // Define a ação de clique para o item inteiro
        holder.itemView.setOnClickListener { item.action() }
    }

    override fun getItemCount() = items.size

    // Mapeia os componentes do XML item_configuracao.xml
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.iv_icon)
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvSubtitle: TextView = view.findViewById(R.id.tv_subtitle)
    }
}