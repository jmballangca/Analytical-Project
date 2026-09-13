package com.ketchupzzz.analytical.presentation.main.finish_game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.presentation.main.games.data.QuizAndLevel
import com.ketchupzzz.analytical.presentation.main.gaming.data.SubmissionData
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.utils.ProgressBar
import com.ketchupzzz.analytical.utils.RatingBar


@Composable
fun FinishGameScreen(
    modifier: Modifier =Modifier,
    args: SubmissionData,
    state: FinishGameState,
    events: (FinishGameEvents) -> Unit,
    navHostController: NavHostController,
) {
    val rememberedArgs = remember { args }

    LaunchedEffect(key1 = rememberedArgs.submissions) {
        if (state.gameSubmitted == null ){
            events(FinishGameEvents.OnSubmit(rememberedArgs.submissions))
        }

    }
    val context = LocalContext.current
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Column(modifier = modifier
            .fillMaxSize()
            .padding(it)
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isLoading -> ProgressBar(title = "Submitting game data...")
                else -> {
                    RewardDisplay(
                        back = {
                            navHostController.navigate(AppRouter.DashboardScreen.route)
                        },
                        restart = {
                            navHostController.navigate(AppRouter.GamingScreen.createRoute(
                                QuizAndLevel(quiz = args.quiz, level = args.levels)
                            ))
                        },
                        next ={
                            navHostController.navigate(AppRouter.GamingScreen.createRoute(
                                QuizAndLevel(quiz = args.quiz, level = args.levels)
                            )) {
                                popUpTo(navHostController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        }
                        ,args = args
                    )
                }
            }
        }
    }
}



@Composable
fun RewardDisplay(
    modifier: Modifier = Modifier,
    back: () -> Unit,
    next: (Levels) -> Unit,
    restart : (Levels) -> Unit,
    args: SubmissionData
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.reward),
                contentDescription = "Rewards",
                contentScale = ContentScale.Crop
            )

            Text(text = "Game over!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Spacer(modifier = modifier.height(16.dp))
            val max = args.levels.points * args.levels.questions
            RatingBar(
                rating = args.submissions.performance.earning,
                onRatingChanged ={},
                maxRating =  max.toDouble(),
                starSize = 36.dp
            )
        }

        // Bottom buttons
        Row(
            modifier = modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FloatingActionButton(
                modifier = modifier.padding(8.dp),
                onClick = {
                    back()
                },
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back",
                    modifier = modifier.size(24.dp)
                )
            }

            FloatingActionButton(
                modifier = modifier.padding(8.dp),
                onClick = {
                    next(args.levels)
                },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_rotate_left_24),
                    contentDescription = "Reset",
                    modifier = modifier.size(24.dp)
                )
            }

            if (args.nextLevels != null) {
                FloatingActionButton(
                    modifier = modifier.padding(8.dp),
                    onClick = {
                        next(args.nextLevels)
                    },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowForwardIos,
                        contentDescription = "Next",
                        modifier = modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
