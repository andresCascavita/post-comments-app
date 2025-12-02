package com.unipiloto.myapplication.presentation.postlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unipiloto.myapplication.presentation.common.components.EmptyScreen
import com.unipiloto.myapplication.presentation.common.components.ErrorScreen
import com.unipiloto.myapplication.presentation.common.components.LoadingScreen
import com.unipiloto.myapplication.presentation.postlist.components.PostItem
import com.unipiloto.myapplication.presentation.postlist.components.SearchBar
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    navController: NavController,
    viewModel: PostListViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = state.error) {
        state.error?.let { error ->
            val result = snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "Reintentar",
                duration = SnackbarDuration.Long
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    viewModel.refreshPosts()
                }
                SnackbarResult.Dismissed -> {
                    viewModel.clearError()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { androidx.compose.material3.Text("Publicaciones") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.refreshPosts() }
            ) {
                Icon(Icons.Default.Refresh, "Sincronizar")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChanged = viewModel::onSearchQueryChanged,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                when {
                    state.isLoading -> {
                        LoadingScreen()
                    }

                    state.error != null && state.posts.isEmpty() -> {
                        ErrorScreen(
                            message = state.error!!,
                            onRetry = { viewModel.refreshPosts() }
                        )
                    }

                    state.posts.isEmpty() -> {
                        EmptyScreen(
                            message = if (state.searchQuery.isNotBlank()) {
                                "No se encontraron resultados para '${state.searchQuery}'"
                            } else {
                                "No hay publicaciones disponibles"
                            }
                        )
                    }

                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            ),
                            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.posts) { post ->
                                PostItem(
                                    post = post,
                                    onClick = {
                                        navController.navigate(
                                            com.unipiloto.myapplication.presentation.navigation.NavRoutes.getPostDetailRoute(
                                                post.id
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}