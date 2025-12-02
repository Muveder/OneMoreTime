package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.GameRepository
import com.example.onemoretime.data.remote.GameDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de la UI para la pantalla Explore.
 */
data class ExploreUiState(
    val games: List<GameDto> = emptyList(),
    val currentPage: Int = 1,
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val error: String? = null
)

class ExploreViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        loadMoreGames(isInitialLoad = true)
    }

    fun loadMoreGames(isInitialLoad: Boolean = false) {
        // CORRECCIÓN: Se elimina la guarda que impedía la carga inicial.
        // La lógica en la UI ya previene cargas múltiples.

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = isInitialLoad, isLoadingMore = !isInitialLoad, error = null)
            }

            val result = try {
                Result.success(gameRepository.getGames(page = _uiState.value.currentPage))
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }

            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { newGames ->
                        currentState.copy(
                            games = currentState.games + newGames,
                            currentPage = currentState.currentPage + 1,
                            isLoading = false,
                            isLoadingMore = false
                        )
                    },
                    onFailure = { error ->
                        currentState.copy(
                            error = error.message,
                            isLoading = false,
                            isLoadingMore = false
                        )
                    }
                )
            }
        }
    }
}
