package com.ketchupzzz.analytical.presentation.main.games.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ketchupzzz.analytical.models.quiz.Quiz

@Composable
fun GameAppBar(quiz: Quiz?, navHostController: NavHostController, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.height(200.dp)
    ) {
        AsyncImage(
            model = quiz?.cover_photo,
            contentDescription = "${quiz?.title} cover",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
            modifier = modifier.fillMaxSize().padding(top = 32.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
        ) {
            val color = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = { navHostController.popBackStack() },colors = color) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "${quiz?.title}", style = MaterialTheme.typography.titleLarge,color = Color.White)
                Text(text = "${quiz?.desc}" , style = MaterialTheme.typography.labelMedium,color = Color.White)
            }
        }
    }
}
