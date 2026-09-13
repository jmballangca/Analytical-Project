package com.ketchupzzz.analytical.presentation.main.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.models.submissions.RecentlyPlayed
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.presentation.main.games.data.QuizWithLevels


@Composable
fun RecentlyPlayed(
    modifier: Modifier = Modifier,
    quizWithLevels: QuizWithLevels,
    submissions : List<Submissions>,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text("Recently Played", style = MaterialTheme.typography.titleLarge)
        AsyncImage(
            quizWithLevels.quiz?.cover_photo,
            error = painterResource(R.drawable.logo),
            placeholder = painterResource(R.drawable.logo),
            modifier = modifier.fillMaxWidth().height(180.dp).clip(
                MaterialTheme.shapes.large
            ),
            contentDescription = "",
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("${quizWithLevels.quiz?.title}", style = MaterialTheme.typography.titleLarge)
        }

    }
}