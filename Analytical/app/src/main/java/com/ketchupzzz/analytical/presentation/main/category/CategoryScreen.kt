package com.ketchupzzz.analytical.presentation.main.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.presentation.main.dashboard.QuizItem
import com.ketchupzzz.analytical.presentation.main.search.GameCard
import com.ketchupzzz.analytical.presentation.main.search.SearchEvents
import com.ketchupzzz.analytical.presentation.main.search.SearchState
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.ui.theme.AnalyticalTheme
import com.ketchupzzz.analytical.utils.ProgressBar
import com.ketchupzzz.analytical.utils.UnknownError
import com.ketchupzzz.analytical.utils.displayCategory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    level: String,
    category : String,
    state: CategoryState,
    events: (CategoryEvents) -> Unit,
    navHostController: NavHostController
) {

    LaunchedEffect(true) {
        events.invoke(CategoryEvents.GetGamesBySchoolLevelAndCategory(category = category, schoolLevel = level))
    }
    when {
        state.isLoading -> ProgressBar(
            title = "Loading"
        )
        state.errors != null  -> UnknownError(
            title = state.errors
        )
        else -> {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                topBar = {
                    TopAppBar(
                        modifier = modifier.padding(8.dp),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        title = {
                            Box(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = MaterialTheme.shapes.medium
                                    )
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${category.displayCategory()}",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium
                                )
                        }
                    })
                }
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(state.games) {
                       QuizItem(item = it) {e->
                           if (it.category == Category.PUZZLE_GAME) {
                               navHostController.navigate(AppRouter.DIFFICULTY.createRoute(it.id ?: ""))
                           } else {
                               navHostController.navigate(AppRouter.GameScreen.createRoute(it.id ?: "","no"))
                           }
                       }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryScreenPrev() {
    AnalyticalTheme {
        CategoryScreen(
            level = "GAGA",
            state = CategoryState(),
            category = "",
            events = {},
            navHostController = rememberNavController()
        )
    }
}