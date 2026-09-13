package com.ketchupzzz.analytical.presentation.main.dashboard

import android.graphics.Color
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.models.displayName
import com.ketchupzzz.analytical.models.quiz.CategoryWithQuiz
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.presentation.main.dashboard.components.DashboardHeader
import com.ketchupzzz.analytical.presentation.main.dashboard.components.RecentlyPlayed
import com.ketchupzzz.analytical.presentation.main.dashboard.components.setColor
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.ui.custom.CircularImageWithStroke
import com.ketchupzzz.analytical.ui.custom.primaryTextFieldColors
import com.ketchupzzz.analytical.ui.theme.AnalyticalTheme
import com.ketchupzzz.analytical.utils.Profile
import com.ketchupzzz.analytical.utils.ProgressBar
import com.ketchupzzz.analytical.utils.UnknownError
import com.ketchupzzz.analytical.utils.displayCategory
import com.ketchupzzz.analytical.utils.toast
import kotlinx.coroutines.launch



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    state: DashboardState,
    events: (DashboardEvents) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(
        state
    ) {
        if (state.students != null) {
            events.invoke(DashboardEvents.OnGetStudentSubmission(state.students.id!!))
        }
    }
    when {
        state.isLoading -> ProgressBar(title="Getting all games..")
        state.quizzes.isEmpty() -> UnknownError(title = "No Games Yet")
        else -> {
            val category = state.quizzes
            val context = LocalContext.current
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    DashboardHeader(
                        students = state.students,
                        submissions = state.submissions,
                        onSearch  ={ navHostController.navigate(AppRouter.Search.route)}
                    )
                }
//                if (state.submissions.isNotEmpty()) {
//                    val submission = state.submissions.firstOrNull()
//                    val game = state.quizzes.filter { quiz ->
//                        quiz.quiz.firstOrNull { it.id == submission?.quizInfo?.id }
//                    }
//                    val submissions  = state.submissions.filter { it.quizInfo?.id == game.quiz }
//                    item(span = { GridItemSpan(2) }) {
//                    }
//                }
                val categories = Category.entries.toList()
                item(
                    span = {GridItemSpan(2)}
                ) {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        modifier = modifier.padding(8.dp)
                    )
                }

                items(categories) {
                    CategoryItem(category = it, imageRes = it.displayImage() , onClick = {
                        navHostController.navigate(AppRouter.Category.navigate(
                            state.students?.schoolLevel?.name ?: "",
                            category = it.name
                        ))
                    })
                }
                item(
                    span = {GridItemSpan(2)}
                ) {
                    Text(
                        text = "Games",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        modifier = modifier.padding(8.dp)
                    )
                }
                items(
                    category[state.selected].quiz,
                    key = {it.id ?: System.currentTimeMillis()}
                ) {game ->
                    QuizItem(item = game) {
                        if (game.category == Category.PUZZLE_GAME) {
                            navHostController.navigate(AppRouter.DIFFICULTY.createRoute(it))
                        } else {
                            navHostController.navigate(AppRouter.GameScreen.createRoute(it,"no"))
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    @DrawableRes  imageRes: Int,
    onClick : () -> Unit
) {
    Card(
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(1.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }

    ) {
        Row(
            modifier = modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .background(
                        color = category.setColor(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = category.name,
                    modifier = modifier.size(32.dp)
                )
            }

            Text(
                text = category.displayName(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

fun Category.displayImage() : Int {
    return when(this) {
        Category.QUIZ_GAME -> R.drawable.rebus
        Category.PUZZLE_GAME -> R.drawable.riddles
        Category.MEMORY_GAME -> R.drawable.word_puzzle
        Category.MATH_GAME -> R.drawable.math
    }
}







@Composable
fun GamesList(categoryWithQuiz: CategoryWithQuiz ,modifier: Modifier = Modifier,navHostController: NavHostController) {
    val list = categoryWithQuiz.quiz
    if (list.isEmpty()) {
        UnknownError(title = "No Games for ${categoryWithQuiz.category} yet!")
        return
    }
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(16.dp)
    ) {

        items(list, key = {it.id!!}) {
            QuizItem(item = it) {
                navHostController.navigate(AppRouter.GameScreen.createRoute(it,"no"))
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    AnalyticalTheme {
        DashboardScreen(navHostController = rememberNavController(), state = DashboardState(), events = {})
    }
}

@Composable
fun RecentActivity(modifier: Modifier = Modifier,state: DashboardState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "Recent Activity", style = MaterialTheme.typography.titleSmall)
        Card(colors = cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.cover),
                    contentDescription = "Cover",
                    modifier = modifier
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Rebus 1", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Level 1", style = MaterialTheme.typography.labelSmall)
                    }
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "Continue")
                    }
                }
            }
        }

    }
}

//@Composable
//fun GamesGrid(
//    modifier: Modifier = Modifier,
//    state : DashboardState
//) {
//    Column(verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
//        Row(
//            modifier = modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(text = "Quiz", style = MaterialTheme.typography.titleSmall)
//            TextButton(onClick = { /*TODO*/ }) {
//                Text(text = "See all")
//            }
//        }
//        val items = Array(100) { "Item $it" }.toList()
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2), // Define the number of columns
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//            horizontalArrangement = Arrangement.spacedBy(16.dp),
//            modifier = Modifier.fillMaxSize()
//        ) {
//            items(items) { item ->
//                QuizItem(item =item)
//            }
//        }
//    }
//
//}


@Composable
fun QuizItem(
    item : Quiz,
    modifier: Modifier = Modifier ,
    e : (id : String) -> Unit
) {
    Column(
        modifier = modifier.clickable {
        item.id?.let {
            e(it)
        }
    }) {
        AsyncImage(
            model = item.cover_photo,
            contentDescription = "${item.title} cover",
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(
                    text = "${item.title}",
                    style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = "${item.levels} Levels", style = MaterialTheme.typography.labelSmall)
            }
            Icon(
                modifier = modifier.size(36.dp),
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "play"
            )
        }


    }
}