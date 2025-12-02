package com.unipiloto.myapplication.presentation.postlist

import com.unipiloto.myapplication.domain.model.Post

data class PostListState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)