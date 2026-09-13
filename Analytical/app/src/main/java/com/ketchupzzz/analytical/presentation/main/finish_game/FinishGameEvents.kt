package com.ketchupzzz.analytical.presentation.main.finish_game

import com.ketchupzzz.analytical.models.submissions.Submissions


sealed interface FinishGameEvents {
    data class OnSubmit(val submissions: Submissions) : FinishGameEvents
}