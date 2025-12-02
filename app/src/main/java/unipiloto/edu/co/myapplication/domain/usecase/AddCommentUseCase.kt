package com.unipiloto.myapplication.domain.usecase

import com.unipiloto.myapplication.domain.model.Comment
import com.unipiloto.myapplication.domain.repository.PostRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(comment: Comment) = repository.addComment(comment)
}