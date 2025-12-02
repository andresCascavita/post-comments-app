package com.unipiloto.myapplication.data.mapper

import com.unipiloto.myapplication.data.local.entity.PostEntity
import com.unipiloto.myapplication.data.remote.dto.PostDto
import com.unipiloto.myapplication.domain.model.Post

object PostMapper {

    // DTO → Entity
    fun fromDtoToEntity(dto: PostDto): PostEntity {
        return PostEntity(
            id = dto.id,
            userId = dto.userId,
            title = dto.title,
            body = dto.body
        )
    }

    // Entity → Domain
    fun fromEntityToDomain(entity: PostEntity): Post {
        return Post(
            id = entity.id,
            userId = entity.userId,
            title = entity.title,
            body = entity.body
        )
    }

    fun fromDomainToEntity(domain: Post): PostEntity {
        return PostEntity(
            id = domain.id,
            userId = domain.userId,
            title = domain.title,
            body = domain.body
        )
    }

    // Función de extensión para DTO
    fun PostDto.toEntity(): PostEntity = fromDtoToEntity(this)

    // Función de extensión para Entity
    fun PostEntity.toDomain(): Post = fromEntityToDomain(this)

    // Función de extensión para Domain
    fun Post.toEntity(): PostEntity = fromDomainToEntity(this)
}