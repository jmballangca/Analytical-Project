package com.ketchupzzz.analytical.presentation.main.gaming.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ketchupzzz.analytical.models.questions.Questions


@Composable
fun ChoicesLayout(
    modifier: Modifier = Modifier,
    question: Questions,
    onNext: (String) -> Unit
) {
    var selected by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(16.dp),
    ) {
        question.choices.forEachIndexed { index, choice ->
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected == choice)
                        Color(0xFFFFA500)
                    else
                        MaterialTheme.colorScheme.surface,
                    contentColor = if (selected == choice)
                        Color.Black
                    else
                        MaterialTheme.colorScheme.onSurface
                ),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                onClick = {
                    selected = choice
                    selected?.let { onNext(it) }
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .size(40.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}.",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Text(text = choice)
                }
            }
        }
    }
}