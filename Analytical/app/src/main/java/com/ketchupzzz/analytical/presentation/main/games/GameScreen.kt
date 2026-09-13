package com.ketchupzzz.analytical.presentation.main.games

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.presentation.main.games.components.GameAppBar
import com.ketchupzzz.analytical.presentation.main.games.components.LevelsPage
import com.ketchupzzz.analytical.presentation.main.games.components.SubmissionsPage
import com.ketchupzzz.analytical.presentation.main.games.data.LevelsWithSubmissions
import com.ketchupzzz.analytical.ui.custom.PrimaryButton
import com.ketchupzzz.analytical.utils.ProgressBar
import com.ketchupzzz.analytical.utils.UnknownError
import kotlinx.coroutines.launch


@Composable
fun GameScreen(
    id : String,
    difficulty : String,
    navHostController: NavHostController,
    state: GameState,
    e : (GameEvents) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(id) {
        if (id.isNotEmpty()){
            e(GameEvents.OnGetGameByID(id))
        }
    }

    when {
        state.isLoading -> ProgressBar(
            title = "Fetching game..."
        )
        state.quiz == null -> UnknownError(title = "Game not found!") {
            PrimaryButton(onClick = { navHostController.popBackStack() }) {
                Text(text = "Back")
            }
        }
        else -> {
            GameDataScreen(
                state = state,
                e = e,
                navHostController = navHostController,
                difficulty = difficulty
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameDataScreen(
    modifier: Modifier = Modifier,
    difficulty: String,
    state: GameState,
    e: (GameEvents) -> Unit,
    navHostController: NavHostController
) {

    val quiz = state.quiz
    val levels = state.levels.sortedBy { it.name }
    val pageState = rememberPagerState(initialPage = 0) { 2 }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { GameAppBar(quiz = quiz, navHostController = navHostController)}
    ) {
        Column(modifier = modifier
            .fillMaxSize()
            .padding(it)) {
            TabRow(selectedTabIndex = pageState.currentPage) {
                Tab(
                    selected = pageState.currentPage == 0,
                    onClick = { scope.launch {
                    pageState.animateScrollToPage(0) }},
                    text = { Text(text = "Levels") }
                )
                Tab(
                    selected = pageState.currentPage == 1,
                    onClick = { scope.launch { pageState.animateScrollToPage(1) }},
                    text = { Text(text = "Submissions")}
                )


            }
            HorizontalPager(state = pageState) {page ->
                val levelsWithSubmissions : MutableList<LevelsWithSubmissions> = mutableListOf()
                for (level in levels) {
                    levelsWithSubmissions.add(
                        LevelsWithSubmissions(
                            levels = level,
                            submissions = state.submissions.filter { it.quizInfo?.levels?.id == level.id }
                        )
                    )
                }

                levelsWithSubmissions.sortedBy { it.levels.levelNumber }
                when(page) {
                    0 -> LevelsPage(
                        quiz= quiz!!,
                        levelsWithSubmissions,
                        state = state,
                        e =  e,
                        navHostController = navHostController,
                        difficulty = difficulty
                    )
                    1 -> SubmissionsPage(
                        s = state,
                        e = e,
                        navHostController = navHostController
                    )
                }
            }
        }
    }
}


