package com.example.onemoretime.data

import com.example.onemoretime.data.remote.GameDto
import com.example.onemoretime.data.remote.RawgApiService

// TODO: En un proyecto real, esta clave NUNCA debe estar aquí.
private const val RAWG_API_KEY = "983ee6a282af43c78ed57d8584bc5249"

/**
 * Interfaz para el repositorio de Juegos de la API de RAWG.
 */
interface GameRepository {
    /**
     * Obtiene una lista de juegos para una página específica desde la API de RAWG.
     */
    suspend fun getGames(page: Int): List<GameDto>
}

/**
 * Implementación del repositorio que se conecta a la API de RAWG.
 */
class NetworkGameRepository(private val rawgApiService: RawgApiService) : GameRepository {
    override suspend fun getGames(page: Int): List<GameDto> {
        return try {
            // Llama al servicio, le pasa la clave y la página, y extrae la lista de 'results'.
            rawgApiService.getGames(apiKey = RAWG_API_KEY, page = page).results
        } catch (e: Exception) {
            // Si hay un error de red, imprimimos el error y devolvemos una lista vacía.
            e.printStackTrace()
            emptyList()
        }
    }
}
