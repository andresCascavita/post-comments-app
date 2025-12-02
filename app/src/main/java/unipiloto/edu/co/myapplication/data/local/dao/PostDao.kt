package com.unipiloto.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unipiloto.myapplication.data.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id ASC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getPostById(id: Int): Flow<PostEntity?>

    @Query("""
        SELECT * FROM posts 
        WHERE title LIKE '%' || :query || '%' 
        OR CAST(id AS TEXT) LIKE '%' || :query || '%'
    """)
    fun searchPosts(query: String): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()
}