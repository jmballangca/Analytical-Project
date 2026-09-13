package com.ketchupzzz.analytical.ui.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ketchupzzz.analytical.R


@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    image : String,
    size : Dp,
) {
    AsyncImage(
        model = image,
        contentDescription = "Avatar",
        placeholder = painterResource(R.drawable.user),
        error = painterResource(R.drawable.user),
        contentScale = ContentScale.Crop,
        modifier = modifier
        .size(size)
        .clip(CircleShape)
        .border(
            color = MaterialTheme.colorScheme.onPrimary,
            width = 3.dp,
            shape = CircleShape
        ))
}