package com.antik.wallet.server.modules.museum.domain

data class MuseumItem(
    val id: Int,
    val title: String,
    val artist: String,
    val medium: String,
    val dimensions: String,
    val url: String,
    val objectDate: String,
    val primaryImage: String,
    val primaryImageSmall: String,
    val repository: String,
    val department: String,
    val creditLine: String,
)
