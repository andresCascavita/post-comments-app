package com.unipiloto.myapplication.presentation.postdetail

import com.unipiloto.myapplication.domain.model.Comment
import com.unipiloto.myapplication.domain.model.Post

data class PostDetailState(
    val post: Post? = null,
    val comments: List<Comment> = emptyList(),
    val isLoadingPost: Boolean = false,
    val isLoadingComments: Boolean = false,
    val isAddingComment: Boolean = false,
    val error: String? = null
)

data class NewCommentState(
    val name: String = "",
    val email: String = "",
    val body: String = "",
    val showValidationErrors: Boolean = false
) {
    val isValid: Boolean
        get() = name.isNotBlank() && email.isNotBlank() && body.isNotBlank() && email.contains("@")

    val nameError: String?
        get() = if (showValidationErrors && name.isBlank()) "Nombre requerido" else null

    val emailError: String?
        get() = when {
            showValidationErrors && email.isBlank() -> "Email requerido"
            showValidationErrors && !email.contains("@") -> "Email inválido"
            else -> null
        }

    val bodyError: String?
        get() = if (showValidationErrors && body.isBlank()) "Comentario requerido" else null
}