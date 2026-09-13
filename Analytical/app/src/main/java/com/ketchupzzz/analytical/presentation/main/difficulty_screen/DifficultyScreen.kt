package com.ketchupzzz.analytical.presentation.main.difficulty_screen

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.presentation.main.games.components.GameAppBar
import com.ketchupzzz.analytical.presentation.main.games.data.QuizAndLevel
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.utils.UnknownError
import com.ketchupzzz.analytical.utils.getMyCurrentLevel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultyScreen(
    modifier: Modifier = Modifier,
    id  : String,
    state : DifficultyState,
    events: (DifficultyEvents) -> Unit,
    navHostController: NavHostController
) {
    LaunchedEffect(id) {
        if (id.isNotEmpty()) {
            events(DifficultyEvents.OnGetGameByID(id))
        }
    }

    val currentLevel = state.levelsWithSubmissions.getMyCurrentLevel()

    Scaffold(
        topBar = { GameAppBar(quiz = state.quiz, navHostController = navHostController) }
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.errors != null -> UnknownError(title = state.errors) {
                    Button(onClick = {navHostController.popBackStack()}) { Text("Back") }
                } else -> {
                    DifficultyCard(
                        difficulty = "Easy",
                        isEnabled = true
                    ) {
                        navHostController.navigate(AppRouter.GameScreen.createRoute(id,"easy"))
                    }
                    DifficultyCard(
                        difficulty = "Medium",
                        isEnabled = currentLevel in 11..20
                    ) {
                        navHostController.navigate(AppRouter.GameScreen.createRoute(id,"medium"))
                    }
                    DifficultyCard(
                        difficulty = "Hard",
                        isEnabled = currentLevel > 21
                    ) {
                        navHostController.navigate(AppRouter.GameScreen.createRoute(id,"hard"))
                    }
                }
            }

        }
    }
}

@Composable
fun DifficultyCard(
    modifier: Modifier = Modifier,
    difficulty : String,
    isEnabled : Boolean,
    onClick : () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier.fillMaxWidth().padding(8.dp).clickable {
            if (isEnabled) {
                onClick()
            }
        }
    ) {
        Row(
            modifier = modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${difficulty}", style = MaterialTheme.typography.titleLarge, modifier = modifier.weight(1f))

            FilledIconButton(
                onClick = {},
            ) {
                Icon(
                    imageVector = if (isEnabled) Icons.Rounded.LockOpen else Icons.Rounded.Lock,
                    contentDescription = "Lock Icon"
                )
            }

        }

    }
}