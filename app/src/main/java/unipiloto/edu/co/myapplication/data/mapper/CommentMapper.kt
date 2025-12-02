package com.unipiloto.myapplication.data.mapper

import com.unipiloto.myapplication.data.local.entity.CommentEntity
import com.unipiloto.myapplication.domain.model.Comment

object CommentMapper {

    // Entity → Domain
    fun fromEntityToDomain(entity: CommentEntity): Comment {
        return Comment(
            id = entity.id,
            postId = entity.postId,
            name = entity.name,
            email = entity.email,
            body = entity.body,
            createdAt = entity.createdAt
        )
    }

    // Domain → Entity
    fun fromDomainToEntity(domain: Comment): CommentEntity {
        return CommentEntity(
            id = domain.id,
            postId = domain.postId,
            name = domain.name,
            email = domain.email,
            body = domain.body,
            createdAt = domain.createdAt
        )
    }

    fun CommentEntity.toDomain(): Comment = fromEntityToDomain(this)

    fun Comment.toEntity(): CommentEntity = fromDomainToEntity(this)
}