package com.ketchupzzz.analytical.models.quiz

import android.os.Parcelable
import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.models.Subject
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Quiz(
    val id : String ? = null,
    val title : String? = null,
    val desc : String ? = null,
    val cover_photo : String ? = null,
    val subject : Subject? = null,
    val category: Category? = null,
    val levels : Int ? = null,
    val visible: Boolean ? = null,
    val createdAt : Date? = null,
) : Parcelable


@Parcelize
data class Levels(
    val id : String = "",
    val name : String = "",
    val levelNumber : Int = 0,
    val points :  Int = 0,
    val questions : Int = 0,
    val timer : Int = 0,
) : Parcelable