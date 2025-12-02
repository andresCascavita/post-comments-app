package com.unipiloto.myapplication.domain.model

data class Comment(
    val id: Int? = null,
    val postId: Int,
    val name: String,
    val email: String,
    val body: String,
    val createdAt: Long = System.currentTimeMillis()
)