package com.ketchupzzz.analytical.presentation.main.crossmath.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun CorrectScreenDialog(
    modifier: Modifier = Modifier,
    totalPoints : Double,
    maxPoints : Int,
    message : String,
    isLast :Boolean,
    onDismiss : () -> Unit,
    onNext : () -> Unit,
    onSubmit : () -> Unit
) {
    val animatedColor = remember { Animatable(initialValue = Color.White) }

    // Launch the animation on composition
    LaunchedEffect(Unit) {
        animatedColor.animateTo(
            targetValue = Color(0xFF273251),
            animationSpec = tween(durationMillis = 2000)
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = animatedColor.value
        ) {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val newMessage = if (!isLast) {
                    message
                } else {
                    "Level Complete"
                }
                Text(newMessage,
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White))
                if (isLast) {
                    Text("Score : ${totalPoints} / ${maxPoints}",
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White))
                }

                Spacer(modifier = modifier.height(16.dp))
                Button(
                    onClick = {
                        if (isLast) {
                            onSubmit()
                        } else {
                            onNext()
                        }
                    }
                ) { Text(
                    text = if (isLast) "Exit Game" else "Next Question"
                ) }
            }
        }
    }
}