package com.horizontenews.app

data class ConfigItem(
    val title: String,
    val subtitle: String,
    val icon: Int,
    val action: () -> Unit
)