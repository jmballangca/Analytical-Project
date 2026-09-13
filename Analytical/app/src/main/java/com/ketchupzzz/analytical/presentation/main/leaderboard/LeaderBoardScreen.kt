package com.ketchupzzz.analytical.presentation.main.leaderboard

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.util.trace
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.StudentsWithSubmissions
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.ui.custom.Avatar
import com.ketchupzzz.analytical.ui.theme.AnalyticalTheme
import com.ketchupzzz.analytical.utils.getStudentFullname
import com.ketchupzzz.analytical.utils.mockLeaderboard


@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    state : LeaderBoardState,
    events: (LeaderboardEvents) -> Unit,
    navHostController: NavHostController,

) {
    val leaderboardData = state.leaderboard.ifEmpty { mockLeaderboard }
    LazyColumn {
        if (state.isLoading) {
            item {
                LinearProgressIndicator(
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
        if (!state.isLoading) {
            item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(
                            bottomStart = 24.dp, bottomEnd = 24.dp
                        )
                    )
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Top Students", style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Black
                ))
                Row(
                    modifier = modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TopUsers(
                        studentWithSubmissions = leaderboardData.getOrNull(1),
                        crown = R.drawable.second,
                        size = 80.dp
                    )
                    TopUsers(
                        studentWithSubmissions = leaderboardData.getOrNull(0),
                        crown = R.drawable.first,
                        size = 100.dp,
                        isFirst = true
                    )
                    TopUsers(
                        studentWithSubmissions = leaderboardData.getOrNull(2),
                        crown = R.drawable.third,
                        size = 80.dp
                    )

                }
            }

            Image(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                    ),
                painter = painterResource(id = R.drawable.group),
                contentScale = ContentScale.FillHeight,
                contentDescription = "test"
                )
            }
        }
            item {
            Text(text = "Leaderboard", style = MaterialTheme.typography.titleLarge.copy(

            ),
                modifier = modifier.padding(8.dp))
        }

            items(leaderboardData) {
                LeaderboardCard(studentWithSubmissions = it)
            }
        }

    }
}


@Composable
fun TopUsers(
    modifier: Modifier = Modifier,
    studentWithSubmissions : StudentsWithSubmissions ?,
    @DrawableRes crown : Int,
    size : Dp,
    isFirst : Boolean = false
) {
    val student = studentWithSubmissions?.students
    val earning = studentWithSubmissions?.totalPoints ?: 0.00
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = crown), contentDescription = "Crown")
        if (isFirst) {
            Spacer(modifier = modifier.height(8.dp))
        }
        Avatar(image = student?.profile?: "", size = size)
        val username = student?.email?.replace("@gmail.com","") ?: "sample"
        val formattedUsername = username.chunked(6).joinToString("\n")
        Text(
            modifier = modifier,
            text = String.format("@%s", formattedUsername),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "$earning",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.Yellow,
                fontWeight = FontWeight.Black,
                shadow = Shadow(
                    color = Color.Gray,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            )
        )

    }
}

@Composable
fun LeaderboardCard(
    modifier: Modifier = Modifier,
    studentWithSubmissions: StudentsWithSubmissions ?,
) {
    val student = studentWithSubmissions?.students
    val earning = studentWithSubmissions?.totalPoints ?: 0.00
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(image = student?.profile ?: "", size = 42.dp)
            val username = student?.getStudentFullname() ?: "Unknown user"
            Text(
                modifier = modifier.weight(1f),
                text = username + "(${student?.schoolLevel?.name})",

                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$earning",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Yellow,
                    fontWeight = FontWeight.Black,
                    shadow = Shadow(
                        color = Color.Gray,
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                )
            )
        }
    }

}
@Preview(showBackground = true)
@Composable
private fun LeaderboardScreenPreview() {
    AnalyticalTheme {
        LeaderboardScreen(state = LeaderBoardState(), events = {}, navHostController = rememberNavController())
    }
}