package com.ketchupzzz.analytical.presentation.main.category

import com.ketchupzzz.analytical.models.SchoolLevel


sealed interface CategoryEvents {
    data class GetGamesBySchoolLevelAndCategory(
        val category : String,
        val schoolLevel: String
    ) : CategoryEvents
}