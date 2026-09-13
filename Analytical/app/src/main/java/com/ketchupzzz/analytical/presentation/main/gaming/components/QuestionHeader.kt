package com.ketchupzzz.analytical.presentation.main.gaming.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.models.questions.Questions


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionHeader(
    modifier: Modifier = Modifier,
    question: Questions,
    answer : String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        if (!question.hint.isNullOrEmpty()) {
            Box(
                modifier = modifier.fillMaxWidth().padding(2.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                HintDialog(hintText = question.hint)
            }
        }
        if (!question.question.isNullOrEmpty()) {
            Text(
                modifier = modifier.fillMaxWidth().padding(4.dp),
                text = "${question.question}",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Black,
                ),
                textAlign = TextAlign.Center
            )
        }


        if (!question.image.isNullOrEmpty()) {
            AsyncImage(
                modifier = modifier
                    .fillMaxSize().clip(MaterialTheme.shapes.large),
                model = question.image,
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.cover),
                placeholder = painterResource(id = R.drawable.cover)
            )
        }
        if (question.choices.isEmpty()) {
            FlowRow(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center

            ) {
                question.answer?.forEachIndexed { index, c ->
                    if (c.toString() == " ") {
                        Spacer(modifier = modifier.width(24.dp))
                    } else {
                        Card(
                            shape = RoundedCornerShape(2.dp),
                            modifier = modifier
                                .size(32.dp)
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                val textByIndex = answer.getOrNull(index)
                                Text(textByIndex?.uppercase() ?: "", style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ))
                            }
                        }
                    }
                }
            }
        }

    }
}