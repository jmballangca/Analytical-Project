package com.ketchupzzz.analytical.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    rating: Double,
    onRatingChanged: (Double) -> Unit,
    modifier: Modifier = Modifier,
    stars: Int = 5,
    starSize: Dp = 32.dp,
    activeColor: Color = Color.Yellow,
    inactiveColor: Color = Color.Gray,
    maxRating: Double = 5.0,
) {
    val starRatingFraction = maxRating / stars

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..stars) {

            val starThreshold = i * starRatingFraction
            val icon = when {
                rating >= starThreshold -> Icons.Rounded.Star // Full star
                rating >= starThreshold - starRatingFraction / 2 -> Icons.Rounded.StarHalf // Half star
                else -> Icons.Rounded.StarBorder // Empty star
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (rating >= starThreshold - starRatingFraction / 2) activeColor else inactiveColor,
                modifier = Modifier
                    .size(starSize)

            )
        }
    }
}

@Composable
fun RatingBar2(
    rating: Double,
    onRatingChanged: (Double) -> Unit,
    modifier: Modifier = Modifier,
    starsCount: Int = 5,
    maxRating: Double = 5.0,
    step: Int = 1,
    starSize: Dp = 32.dp,
    activeColor: Color = Color.Yellow,
    inactiveColor: Color = Color.Gray
) {
    val starRatingFraction = maxRating / starsCount

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..starsCount) {
            val starThreshold = i * starRatingFraction
            val icon = when {
                rating >= starThreshold -> Icons.Rounded.Star // Full star
                rating >= starThreshold - starRatingFraction / 2 -> Icons.Rounded.StarHalf // Half star
                else -> Icons.Rounded.StarBorder // Empty star
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (rating >= starThreshold - starRatingFraction / 2) activeColor else inactiveColor,
                modifier = Modifier
                    .size(starSize)

            )
        }
    }
}
