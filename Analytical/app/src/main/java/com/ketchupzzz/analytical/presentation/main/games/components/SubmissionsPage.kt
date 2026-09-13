package com.ketchupzzz.analytical.presentation.main.games.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.presentation.main.games.GameEvents
import com.ketchupzzz.analytical.presentation.main.games.GameState
import com.ketchupzzz.analytical.presentation.main.games.data.LevelsWithSubmissions
import com.ketchupzzz.analytical.utils.RatingBar
import com.ketchupzzz.analytical.utils.toDisplayDate
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line

fun List<Submissions>.displayLeaderBoard(): List<LevelsWithSubmissions> {
    val groupedSubmissions = this.groupBy { it.quizInfo?.levels }
    val levelWithSubmissions = mutableListOf<LevelsWithSubmissions>()

    groupedSubmissions.forEach { (level, submissions) ->
        if (level != null) {
            val sortedSubmissions = submissions.sortedByDescending { it.createdAt }
            levelWithSubmissions.add(LevelsWithSubmissions(level, sortedSubmissions))
        }
    }

    return levelWithSubmissions
}
@Composable
fun SubmissionsPage(
    modifier: Modifier = Modifier,
    s : GameState,
    e : (GameEvents) -> Unit,
    navHostController: NavHostController
) {
    val submissions = s.submissions.displayLeaderBoard()

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(submissions) {
            SubmissionCard(it) {}
        }
    }
}

@Composable
fun SubmissionCard(
    submissions: LevelsWithSubmissions,
    modifier: Modifier = Modifier,
    onClick : () -> Unit
) {

    val level = submissions.levels
    val submission = submissions.submissions.getOrNull(0)
    Card(

        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
            Column(
                modifier = modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = level.name,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "${submissions.submissions.size} attempts", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }

            Column(
                modifier = modifier
                    .fillMaxHeight()
                    .width(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                graph(values = submissions.submissions)
            }
        }
    }
}


@Composable
fun graph(
    modifier: Modifier = Modifier,
    values : List<Submissions>
) {
    values.sortedBy {
        it.createdAt
    }
    LineChart(
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = false
        ),
        labelHelperProperties = LabelHelperProperties(
            enabled = false
        ),
        dividerProperties = DividerProperties(
            enabled = false
        ),
        gridProperties = GridProperties(
            enabled = false
        ),
        modifier = modifier
            .size(60.dp)
            .background(
                color = Color.Transparent
            ),
        data = listOf(
            Line(
                label = "",
                values = values.map { it.performance.earning },
                color = SolidColor(Color(0xFF23af92)),
                firstGradientFillColor = Color.Transparent,
                secondGradientFillColor = Color.Transparent,
                strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                gradientAnimationDelay = 1000,
                dotProperties = null,
                drawStyle = DrawStyle.Stroke(width = 2.dp),
            )
        ),
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
    )
}