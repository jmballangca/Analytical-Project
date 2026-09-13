package com.ketchupzzz.analytical.models.questions

import android.os.Parcelable
import com.ketchupzzz.analytical.models.Category
import java.util.Date


@kotlinx.parcelize.Parcelize
data class Questions(
    val id : String ? = null,
    val gameID : String ? = null,
    val levelID : String ? = null,
    val image  : String ? = null,
    val question: String ? = null,
    val hint : String ? = null,
    val type : Category? = null,
    val choices : List<String> = mutableListOf(),
    val answer: String ? = null,
    val createdAt : Date = Date(),
    val updatedAt : Date = Date()
): Parcelable


