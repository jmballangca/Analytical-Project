package com.ketchupzzz.analytical.presentation.main.matching_card

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ketchupzzz.analytical.R

@Composable
fun CardFlip(
    modifier: Modifier = Modifier,
    onFlip: (MatchingCard) -> Unit,
    card: MatchingCard,
    haveMatch: Boolean
) {
    val rotation = remember { Animatable(if (card.isFront) 0f else 180f) }
    val backColor = MaterialTheme.colorScheme.primary
    val scope = rememberCoroutineScope()

    // Observe changes to `card.isFront` and animate rotation
    LaunchedEffect(card.isFront) {
        val targetRotation = if (card.isFront) 0f else 180f
        rotation.animateTo(
            targetValue = targetRotation,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = modifier
            .size(90.dp)
            .clickable {
                if (!rotation.isRunning && !haveMatch) {
                    onFlip(card)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .graphicsLayer {
                    rotationY = rotation.value
                    cameraDistance = 12f * density
                }
        ) {
            if (card.isFront) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = 180f }
                        .background(color = MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = card.choice,
                        contentDescription = "Front Image",
                        modifier = Modifier.fillMaxSize(),
                        placeholder = painterResource(R.drawable.logo)
                    )
                }
            } else {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = 180f }
                        .background(backColor),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.back2),
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = "Back Image",
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}


fun List<String>.toMatchingCard() : List<MatchingCard> {
    val cards = mutableListOf<MatchingCard>()

    this.forEachIndexed { index, choice ->
        cards.add(MatchingCard(isFront = true, choice = choice, globalIndex = index * 2))
        cards.add(MatchingCard(isFront = true, choice = choice, globalIndex = index * 2 + 1))
    }

    cards.shuffle()
    return cards
}

