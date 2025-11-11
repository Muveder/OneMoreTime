package com.example.onemoretime.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onemoretime.R
import com.example.onemoretime.model.Post

val cardBackgroundColor = Color(0xFF2C003E)
val onCardColor = Color.White
val communityColor = Color(0xFFB57EDC)

@Composable
fun PostCard(post: Post, navController: androidx.navigation.NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { /* TODO: Navegar al detalle del post */ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Avatar del autor",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("r/${post.community}", color = communityColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(" • posteado por ${post.author} • ${post.timeAgo}", color = Color.Gray, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(post.title, style = MaterialTheme.typography.titleLarge, color = onCardColor, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            /*
            post.image?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "Imagen del post",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            */

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row {
                    Icon(Icons.Default.ThumbUp, contentDescription = "Upvotes", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(post.upvotes.toString(), color = Color.Gray, fontSize = 14.sp)
                }

                Row {
                    Icon(Icons.Default.Comment, contentDescription = "Comentarios", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${post.comments} Comentarios", color = Color.Gray, fontSize = 14.sp)
                }

                StarRating(rating = post.rating)
            }
        }
    }
}

@Composable
fun StarRating(rating: Float) {
    Row {
        for (i in 1..5) {
            val starColor = if (i <= rating) Color(0xFFFFD700) else Color.Gray
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = starColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
