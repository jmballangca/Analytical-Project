package com.ketchupzzz.analytical.presentation.main.crossmath.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ketchupzzz.analytical.presentation.main.gaming.components.HintDialog
import com.ketchupzzz.analytical.ui.theme.AnalyticalTheme


@Composable
fun Calculator(
    modifier: Modifier = Modifier,
    value: String,
    hint : String,
    onChange: (String) -> Unit,
    onDelete: () -> Unit,
    onSubmit: () -> Unit
) {
    val buttons = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    val shapes = RoundedCornerShape(0)
    val itemHeight = 60.dp // Set a fixed height for consistency

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        userScrollEnabled = false,
        columns = GridCells.Fixed(5)
    ) {
        item(span = { GridItemSpan(2) }) {
            TextField(
                value = value,
                onValueChange = { onChange(it) },
                label = { Text("Answer") },
                shape = shapes,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Rounded.Clear, contentDescription = "Erase")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
            )
        }
        item(span = { GridItemSpan(1) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
                    .background(color = MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                HintDialog(hintText = hint)
            }

//            FilledIconButton(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(itemHeight),
//                shape = shapes,
//                onClick = onShowHint
//            ) {
//                Icon(
//                    imageVector = Icons.Rounded.Lightbulb,
//                    contentDescription = "Show Hint"
//                )
//            }
        }
        item(span = { GridItemSpan(2) }) {
            Button(
                onClick = onSubmit,
                shape = shapes,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
            ) {
                Text("ENTER")
            }
        }
        items(buttons) { buttonText ->
            FilledTonalButton(
                shape = shapes,
                onClick = { onChange(buttonText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
            ) {
                Text(buttonText, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun CalculatorPrev() {
    AnalyticalTheme {
        Calculator(
            value = "",
            onDelete = {},
            onChange = {},
            onSubmit = {},
            hint = "",
        )
    }

}