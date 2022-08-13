package com.android.example.recipeapp.presentation.components.shimmer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.example.recipeapp.presentation.BaseApplication

@Composable
fun LoadingShimmerList(
    cardHeight: Dp,
    padding: Dp,
    forRecipeFragment: Boolean = false,
    isDark: Boolean,
    ) {

    val shimmerColorShades: List<Color> = if (!isDark) {
        listOf(
            Color.DarkGray.copy(0.4f),
            Color.White,
            Color.DarkGray.copy(0.4f)
        )
    } else {
        listOf(
            Color.LightGray.copy(0.7f),
            Color.White,
            Color.LightGray.copy(0.7f),
        )
    }


    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1500, delayMillis = 0, easing = LinearEasing),
            RepeatMode.Restart
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerColorShades,
        start = Offset(translateAnim - 200, translateAnim - 200),
        end = Offset(translateAnim, translateAnim),
    )
    val brush2 = Brush.linearGradient(
        colors = shimmerColorShades,
        start = Offset(translateAnim - 300, translateAnim - 300),
        end = Offset(translateAnim - 100, translateAnim - 100)
    )



    if (!forRecipeFragment) {
        LazyColumn(content = {
            items(5) {
                ShimmerCardItem(
                    brush = brush,
                    brush2 = brush2,
                    cardHeight = cardHeight,
                    padding = padding
                )
            }
        })
    } else {
        RecipeFragmentShimmer(cardHeight, brush, brush2, padding)
    }

}


@Composable
fun RecipeFragmentShimmer(
    cardHeight: Dp,
    brush: Brush,
    brush2: Brush,
    padding: Dp
) {
    LazyColumn {
        item {
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
            Spacer(modifier = Modifier.padding(10.dp))
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
            Spacer(modifier = Modifier.padding(10.dp))
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

            Spacer(modifier = Modifier.padding(10.dp))
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
}