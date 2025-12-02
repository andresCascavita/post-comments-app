package com.unipiloto.myapplication.presentation.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unipiloto.myapplication.domain.model.Comment
import com.unipiloto.myapplication.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val repository: PostRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val postId: Int = checkNotNull(savedStateHandle["postId"])

    private val _state = MutableStateFlow(PostDetailState())
    val state: StateFlow<PostDetailState> = _state.asStateFlow()

    private val _newComment = MutableStateFlow(NewCommentState())
    val newComment: StateFlow<NewCommentState> = _newComment.asStateFlow()

    init {
        loadPost()
        loadComments()
    }

    fun loadPost() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingPost = true, error = null) }
            try {
                repository.getPostById(postId)
                    .collectLatest { post ->
                        _state.update { it.copy(post = post, isLoadingPost = false) }
                    }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoadingPost = false,
                        error = e.message ?: "Error al cargar la publicación"
                    )
                }
            }
        }
    }

    private fun loadComments() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingComments = true, error = null) }
            try {
                repository.getCommentsByPostId(postId)
                    .collectLatest { comments ->
                        _state.update { it.copy(comments = comments, isLoadingComments = false) }
                    }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoadingComments = false,
                        error = e.message ?: "Error al cargar comentarios"
                    )
                }
            }
        }
    }

    fun onCommentFieldChanged(field: CommentField, value: String) {
        _newComment.update { current ->
            when (field) {
                CommentField.NAME -> current.copy(name = value)
                CommentField.EMAIL -> current.copy(email = value)
                CommentField.BODY -> current.copy(body = value)
            }
        }
    }

    fun addComment() {
        val currentComment = _newComment.value

        if (!currentComment.isValid) {
            _newComment.update { it.copy(showValidationErrors = true) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isAddingComment = true, error = null) }
            try {
                val comment = Comment(
                    postId = postId,
                    name = currentComment.name,
                    email = currentComment.email,
                    body = currentComment.body
                )

                repository.addComment(comment)

                // Limpiar formulario
                _newComment.update { NewCommentState() }

                // Recargar comentarios
                loadComments()

            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Error al agregar comentario")
                }
            } finally {
                _state.update { it.copy(isAddingComment = false) }
            }
        }
    }

    fun deleteComment(commentId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteComment(commentId)
                loadComments()
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Error al eliminar comentario")
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

enum class CommentField {
    NAME, EMAIL, BODY
}