package com.example.onemoretime.viewmodel

import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.model.Usuario
import com.example.onemoretime.model.Post
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot

class CreatePostViewModelTest : BehaviorSpec({

    lateinit var repository: PostRepository
    lateinit var viewModel: CreatePostViewModel

    beforeTest {
        repository = mockk(relaxed = true)
        viewModel = CreatePostViewModel(repository)
    }

    given("Guardar un post") {

        `when`("los datos son válidos y el usuario existe") {
            val uiState = PostUiState(
                title = "Post de prueba",
                community = "android",
                content = "contenido",
                rating = 4.5f,
                imageUrl = "https://img.com/1.jpg"
            )

            val fakeUser = Usuario(
                id = 1,
                nombre = "Eric",
                correo = "eric@test.com",
                clave = "1234",
                direccion = "Casa 123"
            )

            then("debería guardar el post y retornar true") {
                val slot = slot<Post>()

                val result = viewModel.savePost(uiState, fakeUser)

                result shouldBe true

                coVerify(exactly = 1) {
                    repository.createPost(capture(slot))
                }

                // VALIDAR CAMPOS ENVIADOS AL REPO
                slot.captured.title shouldBe "Post de prueba"
                slot.captured.community shouldBe "android"
                slot.captured.author shouldBe "Eric"
                slot.captured.content shouldBe "contenido"
                slot.captured.rating shouldBe 4.5f
                slot.captured.comments shouldBe 0
                slot.captured.score shouldBe 0
                slot.captured.timeAgo shouldBe "Just now"
            }
        }

        `when`("el usuario es null") {
            val uiState = PostUiState(title = "Algo", community = "android")

            then("no debería guardar y debería retornar false") {
                val result = viewModel.savePost(uiState, null)

                result shouldBe false

                coVerify(exactly = 0) { repository.createPost(any()) }
            }
        }

        `when`("el input está vacío") {
            val uiState = PostUiState(title = "", community = "")

            then("no debería guardar y debería retornar false") {
                val result = viewModel.savePost(uiState, Usuario(1, "A", "a@a.com", "1", "x"))

                result shouldBe false

                coVerify(exactly = 0) { repository.createPost(any()) }
            }
        }
    }
})
