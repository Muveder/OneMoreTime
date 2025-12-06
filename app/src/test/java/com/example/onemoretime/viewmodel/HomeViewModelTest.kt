package com.example.onemoretime.viewmodel

import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.model.Post
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceUntilIdle

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : BehaviorSpec({

    // Dispatcher por spec (no global)
    val dispatcher = StandardTestDispatcher()

    lateinit var repo: PostRepository
    lateinit var postsFlow: MutableStateFlow<List<Post>>

    beforeSpec {
        // Seteamos Main a nuestro dispatcher de prueba
        Dispatchers.setMain(dispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        postsFlow = MutableStateFlow(emptyList())
        repo = mockk(relaxed = true)

        every { repo.getAllPostsStream() } returns postsFlow
    }

    given("HomeViewModel inicializa correctamente") {

        then("debería llamar a refreshPosts() en init") {
            runTest(dispatcher) {
                HomeViewModel(repo)

                // Usamos advanceUntilIdle del TestScope en lugar de dispatcher.scheduler
                advanceUntilIdle()

                coVerify(exactly = 1) { repo.refreshPosts() }
            }
        }
    }

    given("La base de datos emite posts") {

        then("uiState debería actualizar la lista de posts") {
            runTest(dispatcher) {

                val vm = HomeViewModel(repo)

                advanceUntilIdle() // corre el init y sus coroutines

                val fakePosts = listOf(
                    Post(
                        id = 1, title = "A", community = "android",
                        author = "Eric", content = "x",
                        rating = 5f, timeAgo = "1h",
                        comments = 2, score = 10, imageUrl = null
                    )
                )

                // Emitimos los posts
                postsFlow.value = fakePosts

                advanceUntilIdle() // ejecuta el onEach u collectors

                vm.uiState.value.posts shouldBe fakePosts
            }
        }
    }

    given("Se carga más contenido") {

        then("debería solicitar la siguiente página al repositorio") {
            runTest(dispatcher) {

                val vm = HomeViewModel(repo)

                advanceUntilIdle() // init

                vm.loadMorePosts()

                advanceUntilIdle()

                coVerify(exactly = 1) { repo.requestMorePosts(page = 1) }
            }
        }
    }
})
