package com.ketchupzzz.analytical.models

enum class Category {
    QUIZ_GAME, MEMORY_GAME, PUZZLE_GAME, MATH_GAME
}


fun Category.displayName() : String{
    return when(this) {
        Category.QUIZ_GAME -> "QUIZ GAME"
        Category.MEMORY_GAME -> "MEMORY GAME"
        Category.PUZZLE_GAME -> "PUZZLE GAME"
        Category.MATH_GAME -> "MATH GAME"
    }
}

