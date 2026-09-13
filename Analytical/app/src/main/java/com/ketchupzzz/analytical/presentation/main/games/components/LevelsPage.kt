package com.ketchupzzz.analytical.presentation.main.games.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.presentation.main.games.GameEvents
import com.ketchupzzz.analytical.presentation.main.games.GameState
import com.ketchupzzz.analytical.presentation.main.games.data.LevelsWithSubmissions
import com.ketchupzzz.analytical.presentation.main.games.data.QuizAndLevel
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.utils.RatingBar

import com.ketchupzzz.analytical.utils.getHexBackground
import com.ketchupzzz.analytical.utils.getMyCurrentLevel
import kotlinx.coroutines.launch


@Composable
fun LevelsPage(
    quiz: Quiz,
    levels: List<LevelsWithSubmissions>,
    difficulty : String,
    modifier: Modifier = Modifier,
    state: GameState,
    e: (GameEvents) -> Unit,
    navHostController: NavHostController
) {
   val newLevels =  levels.sortedBy {
        it.levels.levelNumber
    }

    val currentLevel = newLevels.getMyCurrentLevel()
    val gameLevels = newLevels.firstOrNull { it.levels.levelNumber == currentLevel }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        val levelDifficulty = when {
            difficulty.equals("easy", ignoreCase = true) -> {
                newLevels.take(10)
            }
            difficulty.equals("medium", ignoreCase = true) -> {
                newLevels.subList(10, minOf(20, newLevels.size))
            }
            difficulty.equals("hard", ignoreCase = true) -> {
                if (newLevels.size > 20) newLevels.subList(20, newLevels.size) else emptyList()
            }
            else -> newLevels
        }

        items(items = levelDifficulty, key = {it.levels.id }) {
            val myLevel: Int = it.levels.levelNumber
            LevelItems(
                quiz = quiz,
                levelsWithSubmissions =it,
                state = state,
                e = e,
                navHostController = navHostController,
                isVisible = myLevel <= currentLevel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelItems(
    quiz: Quiz,
    levelsWithSubmissions: LevelsWithSubmissions,
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    state: GameState,
    e : (GameEvents) -> Unit,
    navHostController: NavHostController
) {
    val levels = levelsWithSubmissions.levels
    val myLevel: Int = levels.name.last().digitToInt()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        StartGame(
            levels = levels,
            sheetState = sheetState,
            onDismiss = { showBottomSheet = false },
            onHide = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            },
            state = state,
            e =e ,
            onStart = {
                when (quiz.category) {
                    Category.MATH_GAME -> {
                        navHostController.navigate(AppRouter.CrossMath.createRoute(
                            QuizAndLevel(quiz = quiz, level = levels)
                        ))
                    }
                    Category.MEMORY_GAME -> {
                        navHostController.navigate(AppRouter.MemoryGame.createRoute(
                            QuizAndLevel(quiz = quiz, level = levels)
                        ))
                    }
                    else -> {
                        navHostController.navigate(AppRouter.GamingScreen.createRoute(
                            QuizAndLevel(quiz = quiz, level = levels)
                        ))
                    }
                }
                showBottomSheet = false
            }
        )
    }
//    if (myLevel  <= currentLevel) {
//        showBottomSheet = true
//    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isVisible) {
                showBottomSheet = true
            }
            .then(
                if (!isVisible) {
                    Modifier.alpha(0.5f)
                } else Modifier
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(levels.levelNumber.getHexBackground()),
                modifier = modifier.size(120.dp),
                contentDescription = "Item card"
            )
            Column(
                modifier = modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "${levels.levelNumber}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Level",
                    style = MaterialTheme.typography.titleSmall
                )
            }

        }
        Spacer(
            modifier = modifier.height(4.dp)
        )
        val rating: Double = if (levelsWithSubmissions.submissions.isNotEmpty()) {
            levelsWithSubmissions.submissions.sortedByDescending { it.performance.earning }[0].performance.earning
        } else {
            0.00
        }
        RatingBar(
            rating = rating,
            onRatingChanged = {  },
            starSize = 16.dp,
            stars = 3,
            maxRating = (levels.points * levels.questions).toDouble()
        )
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartGame(
    levels: Levels,
    sheetState : SheetState,
    onDismiss : () -> Unit,
    onHide: () -> Unit,
    modifier: Modifier = Modifier,
    oldSubmission : Any ? = null,
    state: GameState,
    e : (GameEvents) -> Unit,
    onStart : () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState
    ) {
        when {
            state.isLoading -> CircularProgressIndicator()
            state.errors != null -> Text(text = "${state.errors}")
            else -> {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = levels.name, style = MaterialTheme.typography.titleLarge)
                    Row(
                        modifier = modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val style = MaterialTheme.typography.labelSmall
                        Text(
                            text = "${levels.questions} Questions",
                            style = style
                        )
                        Text(
                            text = "${levels.points * levels.questions} Points",
                            style = style,
                        )
                        Text(
                            text = "${levels.timer} Minutes",
                            style = style
                        )
                    }
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                        OutlinedButton(
                            onClick = { onHide() },
                            shape = RoundedCornerShape(10.dp),
                            modifier = modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }

                        if (oldSubmission != null) {
                            Button(
                                onClick = { /*TODO*/ },
                                shape = RoundedCornerShape(10.dp),
                                modifier = modifier.weight(1f)
                            ) {
                                Text(text = "Restart")
                            }
                        } else {
                            Button(
                                onClick = { onStart() },
                                shape = RoundedCornerShape(10.dp),
                                modifier = modifier.weight(1f)
                            ) {
                                Text(text = "Start")
                            }
                        }

                    }
                }
            }
        }

    }

}




