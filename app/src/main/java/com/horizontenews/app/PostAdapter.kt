class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // ... código anterior ...

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = posts[position]
        val tempoRelativo = post.getTempoRelativo() // Puxa o "há X tempo"

        if (holder is HighlightViewHolder) {
            holder.title.text = post.title
            holder.category.text = post.firstLabel.uppercase()
            holder.date.text = tempoRelativo // Define o tempo no destaque
            Glide.with(holder.itemView.context)
                .load(post.firstImage)
                .centerCrop()
                .into(holder.image)
        } else if (holder is NormalViewHolder) {
            holder.title.text = post.title
            holder.category.text = post.firstLabel.uppercase()
            holder.date.text = tempoRelativo // Define o tempo na lista comum
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
                // Enviamos o tempo relativo para a tela de detalhes também
                putExtra("postDate", tempoRelativo)   
                putExtra("postCategory", post.firstLabel)
            }
            context.startActivity(intent)
        }
    }

    // Atualize os ViewHolders para reconhecer os IDs dos layouts XML
    class HighlightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitleHighlight)
        val category: TextView = view.findViewById(R.id.tvCategoryHighlight)
        val image: ImageView = view.findViewById(R.id.ivHighlight)
        // Certifique-se que este ID existe no item_post_highlight.xml
        val date: TextView = view.findViewById(R.id.tvDateHighlight) 
    }

    class NormalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.postTitle) 
        val category: TextView = view.findViewById(R.id.postCategory)
        val image: ImageView = view.findViewById(R.id.postImage)
        // Certifique-se que este ID existe no item_post.xml
        val date: TextView = view.findViewById(R.id.postDate) 
    }
}