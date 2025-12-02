package com.unipiloto.myapplication.presentation.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unipiloto.myapplication.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PostListState())
    val state: StateFlow<PostListState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadPosts()
        setupSearch()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                repository.getAllPosts()
                    .collectLatest { posts ->
                        _state.update { it.copy(posts = posts, isLoading = false) }
                    }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error al cargar publicaciones"
                    )
                }
            }
        }
    }

    private fun setupSearch() {
        searchJob?.cancel()
        searchJob = _state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(300)
            .onEach { query ->
                if (query.isBlank()) {
                    loadPosts()
                } else {
                    searchPosts(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchPosts(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                repository.searchPosts(query)
                    .collectLatest { posts ->
                        _state.update { it.copy(posts = posts, isLoading = false) }
                    }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error en la búsqueda"
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            try {
                repository.syncPosts()
                loadPosts()
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Error al sincronizar")
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}