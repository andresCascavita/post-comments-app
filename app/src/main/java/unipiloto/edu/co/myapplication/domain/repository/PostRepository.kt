package com.unipiloto.myapplication.domain.repository

import com.unipiloto.myapplication.domain.model.Comment
import com.unipiloto.myapplication.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    // Posts
    fun getAllPosts(): Flow<List<Post>>
    fun getPostById(id: Int): Flow<Post?>
    fun searchPosts(query: String): Flow<List<Post>>

    // Comments
    fun getCommentsByPostId(postId: Int): Flow<List<Comment>>

    // Sync
    suspend fun syncPosts()

    // Operations
    suspend fun addComment(comment: Comment)
    suspend fun deleteComment(commentId: Int)
}