package com.unipiloto.myapplication.presentation.postdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unipiloto.myapplication.presentation.common.components.EmptyScreen
import com.unipiloto.myapplication.presentation.common.components.ErrorScreen
import com.unipiloto.myapplication.presentation.common.components.LoadingScreen
import com.unipiloto.myapplication.presentation.postdetail.components.CommentItem
import com.unipiloto.myapplication.presentation.postdetail.components.NewCommentForm
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    navController: NavController,
    postId: Int? = null,
    viewModel: PostDetailViewModel = hiltViewModel()
) {
    // CORRECCIÓN: Usar collectAsState() en lugar de .value
    val state by viewModel.state.collectAsState()
    val newCommentState by viewModel.newComment.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Manejar errores con Snackbar
    LaunchedEffect(key1 = state.error) {
        state.error?.let { error ->
            val result = snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "OK",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.Dismissed) {
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Publicación #${postId ?: ""}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoadingPost -> {
                    LoadingScreen()
                }

                state.error != null && state.post == null -> {
                    ErrorScreen(
                        message = state.error!!,
                        onRetry = { viewModel.loadPost() }
                    )
                }

                state.post == null -> {
                    EmptyScreen(message = "Publicación no encontrada")
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Detalles de la publicación
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                ) {
                                    Text(
                                        text = state.post!!.title,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = state.post!!.body,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Text(
                                        text = "Comentarios (${state.comments.size})",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        // Formulario para nuevo comentario
                        item {
                            NewCommentForm(
                                state = newCommentState,
                                onFieldChanged = viewModel::onCommentFieldChanged,
                                onAddComment = viewModel::addComment,
                                isAdding = state.isAddingComment
                            )
                        }

                        // Lista de comentarios
                        if (state.isLoadingComments) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        } else if (state.comments.isEmpty()) {
                            item {
                                EmptyScreen(
                                    message = "No hay comentarios aún",
                                    modifier = Modifier.padding(vertical = 32.dp)
                                )
                            }
                        } else {
                            items(state.comments) { comment ->
                                CommentItem(
                                    comment = comment,
                                    onDelete = { comment.id?.let { viewModel.deleteComment(it) } }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}