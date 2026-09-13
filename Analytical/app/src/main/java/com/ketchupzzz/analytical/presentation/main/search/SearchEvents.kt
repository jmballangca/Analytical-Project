package com.ketchupzzz.analytical.presentation.main.search

import com.ketchupzzz.analytical.models.SchoolLevel


sealed interface SearchEvents {
    data object GetAllGames: SearchEvents

    data class OnSearching(val text : String) : SearchEvents

    data object ClearText : SearchEvents
}