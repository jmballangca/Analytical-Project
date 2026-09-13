package com.ketchupzzz.analytical.models.games

import java.util.Date


data class GamesHistory(
    val id : String ? = null,
    val gameID : String? = null,
    val userID : String ? = null,
    val level : Int ? = null,
    val maxLevel : Int ? = null,
    val createdAt : Date = Date(),
    val updatedAt : Date = Date(),
)