package com.unipiloto.myapplication.data.repository

import com.unipiloto.myapplication.data.local.dao.CommentDao
import com.unipiloto.myapplication.data.local.dao.PostDao
import com.unipiloto.myapplication.data.mapper.CommentMapper
import com.unipiloto.myapplication.data.mapper.PostMapper
import com.unipiloto.myapplication.data.remote.api.PostApi
import com.unipiloto.myapplication.domain.model.Comment
import com.unipiloto.myapplication.domain.model.Post
import com.unipiloto.myapplication.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// CLASE CONCRETA - Aquí SÍ puede ir @Inject
class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val commentDao: CommentDao,
    private val postApi: PostApi
) : PostRepository {  // Implementa la interfaz

    override fun getAllPosts(): Flow<List<Post>> {
        return postDao.getAllPosts().map { entities ->
            entities.map { PostMapper.fromEntityToDomain(it) }
        }
    }

    override fun getPostById(id: Int): Flow<Post?> {
        return postDao.getPostById(id).map { entity ->
            entity?.let { PostMapper.fromEntityToDomain(it) }
        }
    }

    override fun searchPosts(query: String): Flow<List<Post>> {
        return postDao.searchPosts(query).map { entities ->
            entities.map { PostMapper.fromEntityToDomain(it) }
        }
    }

    override fun getCommentsByPostId(postId: Int): Flow<List<Comment>> {
        return commentDao.getCommentsByPostId(postId).map { entities ->
            entities.map { CommentMapper.fromEntityToDomain(it) }
        }
    }

    override suspend fun syncPosts() {
        try {
            val response = postApi.getPosts()
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                val entities = posts.map { PostMapper.fromDtoToEntity(it) }
                postDao.insertPosts(entities)
            } else {
                throw Exception("Error al sincronizar: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun addComment(comment: Comment) {
        val entity = CommentMapper.fromDomainToEntity(comment)
        commentDao.insertComment(entity)
    }

    override suspend fun deleteComment(commentId: Int) {
        commentDao.deleteComment(commentId)
    }
}