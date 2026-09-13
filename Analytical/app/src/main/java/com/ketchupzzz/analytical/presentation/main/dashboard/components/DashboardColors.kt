package com.ketchupzzz.analytical.presentation.main.dashboard.components

import androidx.compose.ui.graphics.Color
import com.ketchupzzz.analytical.models.Category

val LightBlue = Color(0xFFC4D0FB)

val LightPink = Color(0xFFFFC2CD)

val LightTeal = Color(0xFF84E6D1)

val LightYellow = Color(0xFFFFD66B)

fun Category.setColor() : Color {
    return when(this) {
        Category.QUIZ_GAME -> LightBlue
        Category.PUZZLE_GAME -> LightTeal
        Category.MEMORY_GAME -> LightPink
        Category.MATH_GAME -> LightYellow
    }
}