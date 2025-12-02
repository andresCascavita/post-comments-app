package com.unipiloto.myapplication.domain.usecase

import com.unipiloto.myapplication.domain.model.Post
import com.unipiloto.myapplication.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(): Flow<List<Post>> = repository.getAllPosts()
}