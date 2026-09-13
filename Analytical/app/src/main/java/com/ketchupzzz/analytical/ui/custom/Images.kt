package com.ketchupzzz.analytical.ui.custom

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun  CircularImageWithStroke(
    image: Painter,
    modifier: Modifier = Modifier,
    imageSize: Dp = 70.dp,
    strokeWidth: Dp = 4.dp,
    strokeColor: Color = Color.White
) {
    Box(
        modifier = modifier.size(imageSize),
        contentAlignment = Alignment.Center,

    ) {
        Canvas(modifier = Modifier.size(imageSize).background(Color.Transparent)) {
            drawCircle(color = strokeColor, radius = size.width / 2f)
        }
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.size(imageSize - (strokeWidth * 2))
        )
    }
}
