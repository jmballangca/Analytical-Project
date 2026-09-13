package com.ketchupzzz.analytical.presentation.main.gaming.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LightbulbCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.ui.theme.AnalyticalTheme


@Composable
fun HintDialog(
    modifier: Modifier = Modifier,
    hintText: String,

) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = !showDialog }) {
            Box(
                modifier = modifier
                    .height(480.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.hint),
                    contentDescription = "Test"
                )
                Text(
                    hintText,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center,

                    ),
                    modifier = modifier.padding(16.dp)
                )
            }
        }
    }
    IconButton(
        onClick = { showDialog = true },
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Icon(imageVector = Icons.Rounded.LightbulbCircle, contentDescription = "Show Hint")
    }
}

@Preview
@Composable
private fun HintPrev() {
    AnalyticalTheme {
        HintDialog(hintText = "Test lang ito ay patunay na test")
    }
}