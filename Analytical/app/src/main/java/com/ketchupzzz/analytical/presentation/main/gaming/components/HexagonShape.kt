package com.ketchupzzz.analytical.presentation.main.gaming.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection


fun HexagonShape(): Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val width = size.width
            val height = size.height
            moveTo(width / 2, 0f)
            lineTo(width, height / 4)
            lineTo(width, 3 * height / 4)
            lineTo(width / 2, height)
            lineTo(0f, 3 * height / 4)
            lineTo(0f, height / 4)
            close()
        }
        return Outline.Generic(path)
    }
}

fun LeftTopQuarterCircle(): Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            // Start at the top-left corner
            moveTo(0f, 0f)
            // Draw the top-left quarter circle
            arcTo(
                rect = Rect(0f, 0f, size.width * 2, size.height * 2),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            // Complete the rectangle for the remaining area
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}