package com.unipiloto.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val postId: Int,
    val name: String,
    val email: String,
    val body: String,
    val createdAt: Long = System.currentTimeMillis()
)