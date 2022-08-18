package com.android.example.recipeapp.presentation.components.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ShimmerState {
    START, END
}

@Composable
fun ShimmerCardItem(
    brush: Brush,
    brush2: Brush,
    cardHeight: Dp,
    padding: Dp
) {
    Column(modifier = Modifier.padding(padding)) {
        Surface(
            shape = MaterialTheme.shapes.small
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight)
                    .background(brush = brush)
                    .padding(padding)
            )
        }
        Spacer(modifier = Modifier.padding(3.dp))
        Surface(
            shape = MaterialTheme.shapes.small
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight / 10)
                    .background(brush = brush2)
                    .padding(padding)
            )
        }
    }
}