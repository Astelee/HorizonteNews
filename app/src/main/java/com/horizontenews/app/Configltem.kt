package com.horizontenews.app

/**
 * Modelo de dados para os itens da lista de configurações.
 * @property title O texto principal do item.
 * @property subtitle Texto secundário (opcional).
 * @property iconRes O ID do recurso do ícone (ex: R.drawable.ic_tema).
 * @property action A função que será executada ao clicar no item.
 */
data class ConfigItem(
    val title: String,
    val subtitle: String? = null,
    val iconRes: Int? = null,
    val action: () -> Unit
)