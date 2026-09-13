package com.ketchupzzz.analytical.models.submissions

import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.models.Subject
import java.util.Date


data class RecentlyPlayed(
    val name : String ? = null,
    val type : Category ? = null,
    val subject : Subject ? = null,
    val image : String ? = null,
    val maxLevel : Int? = null,
    val currentLevel : Int? = null,
    val gameDate : Date ? = null,
)