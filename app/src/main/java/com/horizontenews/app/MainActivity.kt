// ... mantenha os imports ...

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        
        setContentView(R.layout.activity_main)

        verificarPermissaoNotificacao()
        FirebaseMessaging.getInstance().subscribeToTopic("Geral")
        setupToolbar()

        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // --- AJUSTE AQUI: TROCAMOS O BOTTOMNAV PELOS SEUS BOTÕES NAS PONTAS ---
        
        val btnHome = findViewById<android.widget.LinearLayout>(R.id.btn_home)
        val btnMenu = findViewById<android.widget.LinearLayout>(R.id.btn_menu)

        btnHome.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        btnMenu.setOnClickListener {
            val intent = Intent(this, ConfiguracoesActivity::class.java)
            startActivity(intent)
        }

        // Botão da lupa
        val btnOpenSearch = findViewById<ImageButton>(R.id.btn_open_search)
        btnOpenSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        swipeRefreshLayout.setColorSchemeColors(android.graphics.Color.parseColor("#F29121"))
        swipeRefreshLayout.setOnRefreshListener { fetchPosts() }

        fetchPosts()
    }
    
    // ... mantenha o restante das funções (fetchPosts, setupToolbar, etc) ...
}