package com.ketchupzzz.analytical.presentation.main.search

import android.view.RoundedCorner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.models.SchoolLevel
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.utils.ProgressBar
import com.ketchupzzz.analytical.utils.UnknownError


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    state: SearchState,
    events: (SearchEvents) -> Unit,
    navHostController: NavHostController
) {

    when {
        state.isLoading -> ProgressBar(
            title = "Loading"
        )
        state.errors != null  -> UnknownError(
            title = state.errors
        )
        else -> {
            Scaffold(

                topBar = { TopAppBar(
                    title = { Text(text = "Search Games")},
                    navigationIcon = { IconButton(onClick = {  navHostController.popBackStack() }) {
                       Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription ="back" )
                    }}
                )}
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        OutlinedTextField(
                            modifier = modifier.fillMaxWidth(),
                            value = state.searchText,
                            label = {
                                Text(text = "Search Games here")
                            },
                            trailingIcon = {
                                  IconButton(onClick = { events.invoke(SearchEvents.ClearText) }) {
                                      Icon(imageVector = Icons.Rounded.Clear, contentDescription = "Clear")
                                  }
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = ""
                                )
                            },
                            onValueChange = { events(SearchEvents.OnSearching(it) )
                            })
                    }
                    item { 
                        Text(text = "Games (${state.filteredGames.size})", style = MaterialTheme.typography.titleLarge)
                    }
                    items(state.filteredGames) {
                        GameCard(
                            modifier = modifier,
                            quiz = it,
                            onPlay = {
                                if (it.category == Category.PUZZLE_GAME) {
                                    navHostController.navigate(AppRouter.DIFFICULTY.createRoute(it.id ?: ""))
                                } else {
                                    navHostController.navigate(AppRouter.GameScreen.createRoute(it.id ?: "","no"))
                                }
                            }
                        )
                    }
                }
            }
        }
        
    }

}

@Composable
fun GameCard(modifier: Modifier, quiz: Quiz,onPlay: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .wrapContentSize()
            .clickable { onPlay() }
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = quiz.cover_photo,
                contentDescription = "${quiz.title} cover",
                modifier = modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = "${quiz.title}",
                    style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = "${quiz.levels} Levels", style = MaterialTheme.typography.labelSmall)
            }
            Icon(
                modifier = modifier
                    .size(36.dp)
                    .padding(8.dp),
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "play"
            )
        }
    }
}
