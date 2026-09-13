package com.ketchupzzz.analytical.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Students(
    val id : String ? = null,
    val fname : String ? = null,
    val mname : String ? = null,
    val lname : String ? = null,
    val schoolLevel : SchoolLevel? = null,
    val profile : String? = null,
    val email : String? = null,
)  : Parcelable {

}




fun Students.getFullname() : String {
    return "$fname ${mname?.get(0)}. $lname"
}

enum class SchoolLevel(val displayName: String) {
    GRADE_7("Grade 7"),
    GRADE_8("Grade 8"),
    GRADE_9("Grade 9"),
    GRADE_10("Grade 10"),
    GRADE_11("Grade 11"),
    GRADE_12("Grade 12");

    override fun toString(): String {
        return displayName
    }
}



