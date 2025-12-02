package com.unipiloto.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unipiloto.myapplication.data.local.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {

    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY createdAt DESC")
    fun getCommentsByPostId(postId: Int): Flow<List<CommentEntity>>

    @Query("SELECT * FROM comments WHERE id = :id")
    suspend fun getCommentById(id: Int): CommentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    @Query("UPDATE comments SET name = :name, email = :email, body = :body WHERE id = :id")
    suspend fun updateComment(id: Int, name: String, email: String, body: String)

    @Query("DELETE FROM comments WHERE id = :commentId")
    suspend fun deleteComment(commentId: Int)

    @Query("DELETE FROM comments WHERE postId = :postId")
    suspend fun deleteCommentsByPostId(postId: Int)

    @Query("DELETE FROM comments")
    suspend fun deleteAllComments()
}