package com.horizontenews.app

// Classe única para representar um item da lista de configurações
data class ConfigItem(
    val title: String,
    val subtitle: String? = null,
    val iconRes: Int? = null,
    val action: () -> Unit
)