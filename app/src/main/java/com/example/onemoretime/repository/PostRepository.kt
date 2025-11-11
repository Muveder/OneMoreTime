package com.example.onemoretime.repository

import com.example.onemoretime.model.Post

class PostRepository {
    fun getPosts(): List<Post> {
        return listOf(
            Post(
                id = 1,
                author = "Erica",
                community = "eldenring",
                timeAgo = "4h",
                title = "Mi reseña de Elden Ring: Una obra maestra",
                content = "Después de 100 horas de juego, puedo decir que...",
                upvotes = 123,
                comments = 45,
                rating = 5.0f
            ),
            Post(
                id = 2,
                author = "Carlos",
                community = "cyberpunk2077",
                timeAgo = "1d",
                title = "Cyberpunk 2077 con el parche 2.0 es otro juego",
                content = "La redención de CD Projekt Red es real. Night City se siente más viva que nunca.",
                upvotes = 88,
                comments = 23,
                rating = 4.5f
            ),
            Post(
                id = 3,
                author = "Ana",
                community = "stardewvalley",
                timeAgo = "2d",
                title = "Enganchada a Stardew Valley otra vez",
                content = "No importa cuántas veces lo juegue, siempre vuelvo a mi granja.",
                upvotes = 250,
                comments = 78,
                rating = 5.0f
            )
        )
    }
}
