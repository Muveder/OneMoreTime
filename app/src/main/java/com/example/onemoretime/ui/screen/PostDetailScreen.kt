package com.example.onemoretime.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.onemoretime.model.Comment
import com.example.onemoretime.viewmodel.AppViewModelProvider
import com.example.onemoretime.viewmodel.CommentNode
import com.example.onemoretime.viewmodel.PostDetailViewModel
import com.example.onemoretime.viewmodel.VoteType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    navController: NavController,
    viewModel: PostDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val post = uiState.post

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(post?.community ?: "Detalle", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkPurple)
            )
        },
        bottomBar = {
            NewCommentForm(viewModel = viewModel)
        }
    ) { paddingValues ->
        if (post != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // --- Contenido del Post ---
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = post.title, style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "por ${post.author}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        post.content?.let {
                            Text(text = it, style = MaterialTheme.typography.bodyLarge)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        PostVoteBar(viewModel = viewModel, score = post.score)
                    }
                }

                item { Divider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.5f)) }

                // --- Árbol de Comentarios ---
                items(uiState.commentTree) { commentNode ->
                    CommentThread(commentNode = commentNode)
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun CommentThread(commentNode: CommentNode, depth: Int = 0) {
    Column(
        modifier = Modifier.padding(start = (16 * depth).dp)
    ) {
        CommentItem(comment = commentNode.comment)
        commentNode.replies.forEach { replyNode ->
            CommentThread(commentNode = replyNode, depth = depth + 1)
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = comment.author, fontWeight = FontWeight.Bold)
        Text(text = comment.content)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.SentimentVerySatisfied, contentDescription = "Upvote", modifier = Modifier.size(16.dp))
            Text(" ${comment.score} ", style = MaterialTheme.typography.bodySmall)
            Icon(Icons.Default.SentimentVeryDissatisfied, contentDescription = "Downvote", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.AutoMirrored.Filled.Reply, contentDescription = "Responder", modifier = Modifier.size(16.dp))
            Text(" Responder", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun PostVoteBar(viewModel: PostDetailViewModel, score: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = { viewModel.vote(VoteType.UPVOTE) }) {
            Icon(Icons.Default.SentimentVerySatisfied, contentDescription = "Voto positivo", modifier = Modifier.size(28.dp))
        }
        Text(text = score.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
        IconButton(onClick = { viewModel.vote(VoteType.DOWNVOTE) }) {
            Icon(Icons.Default.SentimentVeryDissatisfied, contentDescription = "Voto negativo", modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun NewCommentForm(viewModel: PostDetailViewModel) {
    OutlinedTextField(
        value = viewModel.newCommentText,
        onValueChange = { viewModel.onNewCommentChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)),
        placeholder = { Text("Añade un comentario...") },
        trailingIcon = {
            IconButton(onClick = { viewModel.postComment() }) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar comentario")
            }
        }
    )
}
