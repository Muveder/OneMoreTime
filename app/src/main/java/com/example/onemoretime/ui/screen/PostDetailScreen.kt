package com.example.onemoretime.ui.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
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
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> /* Opcional: manejar denegación */ }
    )

    LaunchedEffect(Unit) {
        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.post?.community ?: "", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkPurple),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = { NewCommentForm(viewModel = viewModel, isReplyingTo = uiState.replyingTo) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (post != null) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Text(text = post.title, style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "por ${post.author}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(12.dp))

                        post.imageUrl?.let {
                            Image(
                                painter = rememberAsyncImagePainter(model = Uri.parse(it)),
                                contentDescription = "Imagen de la reseña",
                                modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        post.content?.let {
                            Text(text = it, style = MaterialTheme.typography.bodyLarge)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        PostVoteBar(viewModel = viewModel, score = post.score, context = context)
                    }
                }
                item { Divider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.5f)) }

                items(uiState.commentTree) {
                    CommentThread(commentNode = it, viewModel = viewModel)
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("No se pudo cargar la reseña.")
            }
        }
    }
}

@Composable
fun CommentThread(commentNode: CommentNode, viewModel: PostDetailViewModel, depth: Int = 0) {
    Column(modifier = Modifier.padding(start = (16 * depth).dp)) {
        CommentItem(comment = commentNode.comment, viewModel = viewModel)
        commentNode.replies.forEach { replyNode ->
            CommentThread(commentNode = replyNode, viewModel = viewModel, depth = depth + 1)
        }
    }
}

@Composable
fun CommentItem(comment: Comment, viewModel: PostDetailViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = comment.author, fontWeight = FontWeight.Bold)
        Text(text = comment.content)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.voteOnComment(comment, VoteType.UPVOTE) }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.SentimentVerySatisfied, contentDescription = "Upvote", modifier = Modifier.size(16.dp))
            }
            Text(" ${comment.score} ", style = MaterialTheme.typography.bodySmall)
            IconButton(onClick = { viewModel.voteOnComment(comment, VoteType.DOWNVOTE) }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.SentimentVeryDissatisfied, contentDescription = "Downvote", modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Row(
                modifier = Modifier.clickable { viewModel.onReplyClicked(comment) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Filled.Reply, contentDescription = "Responder", modifier = Modifier.size(16.dp))
                Text(" Responder", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun PostVoteBar(viewModel: PostDetailViewModel, score: Int, context: Context) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = { viewModel.voteOnPost(VoteType.UPVOTE, context) }) {
            Icon(Icons.Default.SentimentVerySatisfied, contentDescription = "Voto positivo", modifier = Modifier.size(28.dp))
        }
        Text(text = score.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
        IconButton(onClick = { viewModel.voteOnPost(VoteType.DOWNVOTE, context) }) {
            Icon(Icons.Default.SentimentVeryDissatisfied, contentDescription = "Voto negativo", modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun NewCommentForm(viewModel: PostDetailViewModel, isReplyingTo: Comment?) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
        if (isReplyingTo != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Respondiendo a @${isReplyingTo.author}", style = MaterialTheme.typography.bodySmall)
                IconButton(onClick = { viewModel.cancelReply() }, modifier = Modifier.size(16.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Cancelar respuesta")
                }
            }
        }
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
}
