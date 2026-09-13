package com.ketchupzzz.analytical.presentation.main.gaming.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.presentation.main.gaming.GamingEvents
import com.ketchupzzz.analytical.presentation.main.gaming.GamingState
import com.ketchupzzz.analytical.ui.theme.AnalyticalTheme
import com.ketchupzzz.analytical.utils.RatingBar
import com.ketchupzzz.analytical.utils.getEarnings
import com.ketchupzzz.analytical.utils.getEndMinute


@Composable
fun TimeoutDialog(
    modifier: Modifier = Modifier,
    levels: Levels,
    state : GamingState,
    events: (GamingEvents) -> Unit,
    onDismissRequest: () -> Unit,
    onNext : () -> Unit,
    onRestart : () -> Unit,
    onBack : () -> Unit
) {
    val score = state.answerSheet.getEarnings()
    val time = state.timer.getEndMinute()
    val max = levels.points * levels.questions
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(
            modifier = modifier
                .height(480.dp)
                .fillMaxWidth()
        ) {
            Image(
                modifier = modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.failed),
                contentDescription = "Test"
            )
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 52.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                RatingBar(
                    rating = state.answerSheet.getEarnings(),
                    onRatingChanged ={},
                    stars = 3,
                    maxRating =  max.toDouble(),
                    starSize = 50.dp
                )
                Row(
                    modifier = modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Points",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )

                        Text(
                            score.toString(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Time",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            time,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = modifier.height(42.dp).clickable {
                            onBack()
                        },
                        painter = painterResource(id = R.drawable.back_cover),
                        contentDescription = "Back"
                    )
                    Spacer(modifier = modifier.width(12.dp))
                    Image(
                        modifier = modifier.height(42.dp).clickable {
                            onRestart()
                        },
                        painter = painterResource(id = R.drawable.retry),
                        contentDescription = "Restart"
                    )
                }
            }
        }
    }
}



@Preview
@Composable
private fun TimeOutPrev() {
    AnalyticalTheme {
        TimeoutDialog(
            state =  GamingState(),
            events = {},
            onRestart = {},
            onNext ={},
            onBack = {},
            levels = Levels(),
            onDismissRequest = {}
        )
    }
}