package com.unipiloto.myapplication.domain.usecase

import com.unipiloto.myapplication.domain.model.Comment
import com.unipiloto.myapplication.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(postId: Int): Flow<List<Comment>> =
        repository.getCommentsByPostId(postId)
}