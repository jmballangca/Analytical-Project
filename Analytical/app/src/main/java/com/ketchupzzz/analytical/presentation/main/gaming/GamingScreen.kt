package com.ketchupzzz.analytical.presentation.main.gaming

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.presentation.main.games.data.QuizAndLevel
import com.ketchupzzz.analytical.presentation.main.gaming.components.ChoicesGrid
import com.ketchupzzz.analytical.presentation.main.gaming.components.ChoicesLayout
import com.ketchupzzz.analytical.presentation.main.gaming.components.FinishDialog
import com.ketchupzzz.analytical.presentation.main.gaming.components.QuestionHeader
import com.ketchupzzz.analytical.presentation.main.gaming.components.TimeoutDialog
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.ui.theme.AnalyticalTheme
import com.ketchupzzz.analytical.utils.ProgressBar
import com.ketchupzzz.analytical.utils.UnknownError
import com.ketchupzzz.analytical.utils.shuffleString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GamingScreen(
    modifier: Modifier = Modifier,
    s : GamingState,
    e : (GamingEvents) -> Unit,
    navHostController: NavHostController,
    args : QuizAndLevel
) {
    val quiz = args.quiz
    val level = args.level
    val nextLevel = args.nextLevels
    val context = LocalContext.current
    LaunchedEffect(args) {
        e(GamingEvents.OnGetQuestions(quiz.id!!,level.id,level.questions,level.timer))
    }
    var finishDialog by remember {
        mutableStateOf(false)
    }

    var outOfTimeDialog by remember {
        mutableStateOf(false)
    }
    val pageState = rememberPagerState(initialPage = 0) { level.questions }
    val scope = rememberCoroutineScope()

    LaunchedEffect(s.timer) {
        if (s.timer == 0) {
            e(GamingEvents.OnSubmit(
                level = level,
                quiz = quiz,
                context = context
            ))
            outOfTimeDialog = true
        }
    }

    if (outOfTimeDialog) {
        TimeoutDialog(
            state = s,
            events = e,
            onBack = {
                outOfTimeDialog = false
                navHostController.popBackStack()
            },
            onRestart = {
                navHostController.navigate(
                    AppRouter.GamingScreen.createRoute(QuizAndLevel(
                        quiz = quiz,
                        level = level ,
                        nextLevels = nextLevel
                    )
                ))
            },
            onNext = {
            },
            levels = level,
            onDismissRequest = {
                outOfTimeDialog = false
                navHostController.popBackStack()
            }
        )
    }
    if (finishDialog) {
        FinishDialog(
            levels = level,
            state = s,
            events = e,
            onDismissRequest = {
                finishDialog = false
                navHostController.popBackStack()
                },
            onNext = { },
            onRestart = {   navHostController.navigate(
                AppRouter.GamingScreen.createRoute(QuizAndLevel(
                    quiz = quiz,
                    level = level ,
                    nextLevels = nextLevel
                )
                )) },
            onBack = {
                finishDialog = false
                navHostController.popBackStack()
            })
    }
    BackpressHandler(navHostController = navHostController, onBackPress = {
        e(GamingEvents.OnExitGame(navHostController))
    })
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        topBar = {
            if (s.questions.isNotEmpty() && s.questions.size == level.questions) {
                GamingTopBar(
                    level = level,
                    index = pageState.currentPage + 1,
                    s = s,
                    e=e,
                    stop = finishDialog
                )
            }
        }
    ) {
            when {
                s.isLoading -> ProgressBar(
                    title = "Getting Questions"
                )
                s.questions.isEmpty() || s.questions.size < level.questions -> UnknownError(
                    title = if (s.questions.isEmpty()) {
                        "No questions yet!"
                    } else {
                        "Not enough questions"
                    }
                ) { Button(onClick = { navHostController.popBackStack() }) {
                    Text(text = "Back")
                }} else -> {
                HorizontalPager(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(it),
                    state = pageState,
                    userScrollEnabled = false
                ) { index ->
                    QuestionScreen(
                        question  = s.questions[index],
                        state = s,
                        events = e,
                        onNext = { ans->
                            val points = if (s.questions[index].answer?.lowercase()?.trim() == ans.lowercase().trim()) {
                                level.points
                            } else {
                                0
                            }
                            e(GamingEvents.OnSelectAnswer(
                                s.questions[index],
                                ans,
                                points
                            ))
                            scope.launch {
                                if (index < s.questions.size - 1) {
                                    pageState.animateScrollToPage(index + 1)
                                    e(GamingEvents.OnReset)
                                } else {
                                    e(GamingEvents.OnReset)
                                    finishDialog = true

                                    e(GamingEvents.OnSubmit(
                                        level = level,
                                        quiz = quiz,
                                        context = context
                                    ))
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionScreen(
    modifier: Modifier = Modifier,
    question: Questions,
    state: GamingState,
    events: (GamingEvents) -> Unit,
    onNext: (String) -> Unit,
) {
    var shuffledString by remember {
        mutableStateOf(question.answer?.shuffleString())
    }

    var answer by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(answer) {
        if (answer.trim() == question.answer?.trim()) {
            onNext(answer)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        QuestionHeader(
            question = question,
            answer = answer,
        )
        Spacer(
            modifier = modifier.height(16.dp)
        )
        if (question.choices.isEmpty()) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "${answer.uppercase()}")
                    IconButton(
                        onClick = {
                            shuffledString = question.answer
                                ?.shuffleString()
                            answer = ""
                            events(GamingEvents.OnReset)
                        }) {
                        Icon(imageVector =     Icons.Default.Replay, contentDescription = "reset")
                    }
                }
                ChoicesGrid(
                    text = shuffledString ?:"",
                    selectedIndexes = state.answerIndex,
                    onClick = { text ,index ->
                        events.invoke(GamingEvents.OnAnswerChanged(text, index))
                        if (question.answer?.getOrNull(answer.length) == ' ') {
                            answer += " "
                        }
                        answer += text
                    }
                )
                if (shuffledString.isNullOrEmpty() && answer.trim() != question.answer?.trim()) {
                    shuffledString = question.answer
                        ?.shuffleString()
                    answer = ""
                }
            }

        } else {
            ChoicesLayout(
                question = question,
                onNext = { s ->
                    answer = s
                    scope.launch {
                        delay(1000)
                        onNext(answer)
                    }

                }
            )
        }
    }
}







@Preview(showBackground = true)
@Composable
private fun QuestionScreenPrev() {
    AnalyticalTheme {
        val question = Questions(
            id = "FE0FkyGHrmIBzYai6RVl",
            answer = "cry",
            image = "https://firebasestorage.googleapis.com/v0/b/analytical-8e3db.appspot.com/o/quiz%2Fquestions%2F4fcdd243-af0a-4939-9253-cdbf0bdd1bef?alt=media&token=05465ebd-9d35-499b-831f-8f41757e47a1",
            question = "",
            choices = listOf("Answer 1","Answer 2","Answer 3" ,"Answer 4"),
            type = Category.PUZZLE_GAME,
            hint = "A sob is an intense form of this action, just like scrubbing is a thorough form of washing.",
        )
        QuestionScreen(question = question, state = GamingState(), events = {}) {

        }
    }
}




@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamingTopBar(
    level : Levels,
    index : Int,
    s: GamingState,
    e : (GamingEvents) -> Unit,
    modifier: Modifier = Modifier,
    stop : Boolean = false
) {
    var timer = s.timer

    LaunchedEffect(timer) {
        while (timer > 0 && !stop) {
            delay(1000L)
            timer--
            e(GamingEvents.OnUpdateTimer(timer))
        }
    }
    val minutes = timer / 60
    val seconds = timer % 60
    val clock = String.format("%02d:%02d", minutes, seconds)

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val style = MaterialTheme.typography.titleMedium
            Text(text = level.name, style = style, fontWeight = FontWeight.Bold)
            Text(text = "$index / ${level.questions}", style = style , fontWeight = FontWeight.Bold)
            Box(modifier = modifier
                .wrapContentSize()
                .padding(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                ),
            ) {
                Text(text = "${clock}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Light,
                    modifier = modifier.padding(8.dp)
                )
            }
        }
    })

}

@Composable
fun BackpressHandler(
    navHostController: NavHostController,
    onBackPress : () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var backPressCount by remember { mutableIntStateOf(0) }

    // Handle back press events
    BackHandler {
        if (backPressCount > 0) {
            showDialog = true
        } else {
            backPressCount++
        }
    }

    LaunchedEffect(backPressCount) {
        if (backPressCount == 2) {
            delay(1000)
            backPressCount = 0
        }
    }

    if (showDialog) {
        ExitDialog(
            onConfirm = {
                showDialog = false
                onBackPress()
            },
            onDismiss = { showDialog = false }
        )
    }
}



@Composable
fun ExitDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirm Exit") },
        text = { Text(text = "Are you sure you want to exit the game?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}