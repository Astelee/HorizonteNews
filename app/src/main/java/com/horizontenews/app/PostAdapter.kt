class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Definimos dois tipos de linha
    private val TYPE_HIGHLIGHT = 0
    private val TYPE_NORMAL = 1

    override fun getItemViewType(position: Int): Int {
        // Se for a primeira notícia, retorna tipo Destaque
        return if (position == 0) TYPE_HIGHLIGHT else TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HIGHLIGHT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_highlight, parent, false)
            HighlightViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
            NormalViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = posts[position]
        
        if (holder is HighlightViewHolder) {
            holder.title.text = post.title
            holder.category.text = post.category.uppercase()
            Glide.with(holder.itemView.context).load(post.imageUrl).into(holder.image)
        } else if (holder is NormalViewHolder) {
            holder.title.text = post.title
            holder.category.text = post.category.uppercase()
            Glide.with(holder.itemView.context).load(post.imageUrl).into(holder.image)
        }
        
        // Clique para abrir o detalhe
        holder.itemView.setOnClickListener {
            // Seu código de Intent para DetailActivity aqui
        }
    }

    override fun getItemCount() = posts.size

    // ViewHolder para o Destaque
    class HighlightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitleHighlight)
        val category: TextView = view.findViewById(R.id.tvCategoryHighlight)
        val image: ImageView = view.findViewById(R.id.ivHighlight)
    }

    // ViewHolder para a lista normal (o que você já tem)
    class NormalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.postTitle) // Ajuste para seus IDs reais
        val category: TextView = view.findViewById(R.id.postCategory)
        val image: ImageView = view.findViewById(R.id.postImage)
    }
}